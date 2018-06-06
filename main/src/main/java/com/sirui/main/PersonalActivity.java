package com.sirui.main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.net.client.HttpDialogListener;
import com.net.client.HttpGo;
import com.sirui.basiclib.BaseActivity;
import com.sirui.basiclib.config.NetUrl;
import com.sirui.basiclib.config.SRConstant;
import com.sirui.basiclib.data.DataManager;
import com.sirui.basiclib.data.bean.User;
import com.sirui.basiclib.http.JsonUtil;
import com.sirui.basiclib.ui.InputActivity;
import com.sirui.basiclib.utils.KeyboardUtils;
import com.sirui.basiclib.utils.MyLog;
import com.sirui.basiclib.utils.ViewUtil;
import com.sirui.basiclib.utils.string.StringUtil;
import com.sirui.basiclib.widget.DialogUtil;
import com.sirui.basiclib.widget.MyToast;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author hw
 *         切换Fragment
 */
public class PersonalActivity extends BaseActivity{

    private static final int INPUT_NAME_REQUEST_CODE = 10;
    private static final int INPUT_AGE_REQUEST_CODE = 11;

    private User mUser;

    @BindView(R2.id.tv_name)
    TextView tvName;
    @BindView(R2.id.tv_age)
    TextView tvAge;
    @BindView(R2.id.tv_user_sex)
    TextView tvUserSex;
    @BindView(R2.id.tv_phone_content)
    TextView tvPhoneContent;
/*    @BindView(R2.id.tv_name_error)
    TextView nameErrTV;
    @BindView(R2.id.tv_age_error)
    TextView birthErrTV;*/
    //    @BindView(R2.id.btn_modify_commit)
//    Button modifiedText;
//    @BindView(R2.id.tv_right_button)
//    TextView tvRightButton;
//    @BindView(R2.id.rg_gender)
//    RadioGroup rgGender;
//    @BindView(R2.id.rb_male)
//    RadioButton rbMale;
//    @BindView(R2.id.rb_female)
//    RadioButton rbFemale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal;
    }

    @Override
    protected void iniData() {
        mUser = DataManager.getInstance().getUser();
    }

    @Override
    protected void iniView() {
        initTitle("个人信息");
        if(mUser != null){
            initInfoDisplay(mUser);
        }
    }


    private void setEditListener() {
//        tvRightButton.setVisibility(View.VISIBLE);
//        Drawable drawable=getResources().getDrawable(R.drawable.icon_edit_new);
//        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
//        tvRightButton.setCompoundDrawables(drawable,null,null,null);
//        etName.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (!isInEdit)
//                    return;
//                checkName(charSequence.toString());
////                checkInput();
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//        etAge.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (!isInEdit)
//                    return;
//                checkAge(charSequence.toString());
////                checkInput();
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
    }

    private boolean checkName(String name) {
         if(TextUtils.isEmpty(name)){
             return false;
         }
        return Pattern.matches("[a-zA-Z.·\\p{Han}]{2,}",name);
    }

    private boolean checkAge(String birth) {
        return Pattern.matches("^[1-9][0-9]?$", birth);
    }

    @OnClick({R2.id.tv_name,R2.id.tv_age,R2.id.tv_user_sex})
    public void onClicked(View view){
        int id = view.getId();
        if(id == R.id.tv_name){
            InputActivity.start(this,INPUT_NAME_REQUEST_CODE,"姓名",tvName.getText().toString());
        }else if(id == R.id.tv_age){
            InputActivity.start(this,INPUT_AGE_REQUEST_CODE,"年龄",tvAge.getText().toString());
        }else if(id == R.id.tv_user_sex){
            DialogUtil.showSexDialog(PersonalActivity.this, new DialogUtil.onSexSelectedListener() {
                @Override
                public void onSelected(int index) {
                     mUser.setSex(String.valueOf(index));
                     updateUserInfo(mUser);
                }
            });
        }
    }
//    public void onModifiedClick(View view) {
//        if(view.getId() == R.id.tv_right_button){
//             ViewUtil.setPostEnableClick(tvRightButton);
//            if (tvRightButton.getText().toString().equals("保存")){
//                User user = modifyUserInfo(loginUser);
//                if (user != null) {
//                    updateUserInfo(loginUser);
//                } else {
//                    return;
//                }
//            }else {
//                tvRightButton.setText("保存");
//                setModified(true, loginUser);
//                showConfirmBtn();
//                checkData();
//                KeyboardUtils.showSoftInput(this, etName);
//                ViewUtil.setSelectionEnd(etName);
//            }
//        }
//    }


    private void checkData() {
        String name = tvName.getText().toString();
        String age = tvAge.getText().toString();
        checkName(name);
        checkAge(age);
//        checkInput();
    }

    private void showConfirmBtn() {
//        tvRightButton.setEnabled(false);
    }

    private void showModifyBtn() {
//        tvRightButton.setEnabled(true);
    }

//    @OnClick({R2.id.rb_male, R2.id.rb_female})
//    public void onGenderClick(View view) {
//        boolean checked = ((RadioButton) view).isChecked();
//        int id = view.getId();
//        if(id == R.id.rb_female){
//            if(checked){
//                tvUserGender.setText(R.string.girl);
//            }
//        }else if(id == R.id.rb_male){
//            if (checked) {
//                tvUserGender.setText(R.string.boy);
//            }
//        }
//    }

    /**
     * 初始化界面信息显示
     *
     * @param user 需要显示的用户信息
     */
    private void initInfoDisplay(User user) {
        MyLog.i("----initInfoDisplay----");
        if (user != null) {
            tvName.setText(user.getRealName());
            tvPhoneContent.setText(user.getPatientMobile());
            tvAge.setText(user.getAge());
            if (user.getSex().equals(SRConstant.SEX_GIRL)) {
                tvUserSex.setText(R.string.girl);
            } else {
                tvUserSex.setText(R.string.boy);
            }
        } else {
            MyLog.i("----loginUser = null ----");
        }
    }

//    /**
//     * 遮盖用户敏感信息
//     * @param user 用户对象
//     */
//    private void hideUserInfo(@NonNull User user) {
//        etName.setText(StringUtil.hideRealName(user.getRealName()));
//        tvPhoneContent.setText(StringUtil.hideMobileNumber(user.getMobileNo()));
//    }

    /**
     * 设置用户信息是否可以编辑
     *
     * @param enable 是否可编辑
     * @param user   回显用户对象，可能为空
     */
//    private void setModified(boolean enable, @Nullable User user) {
//        if (user != null) {
//            if (enable) {
//                etName.requestFocus();
//                rgGender.setVisibility(View.VISIBLE);
//                tvUserGender.setVisibility(View.GONE);
////                tvAgeUnit.setVisibility(View.VISIBLE);
//            } else {
//                rgGender.setVisibility(View.GONE);
//                tvUserGender.setVisibility(View.VISIBLE);
////                tvAgeUnit.setVisibility(View.GONE);
//                etName.setText(user.getRealName());
//                tvPhoneContent.setText(user.getPatientMobile());
//            }
//            etName.setEnabled(enable);
//            etAge.setEnabled(enable);
//        }
//    }

    /**
     * 校验用户信息
     *
     * @return 用户信息是否合理合法
     */

//    private boolean validateUserInfo(String cartNo) {
//        if (!IDCardUtil.validate(cartNo)) {
//            DialogUtil.confirm("请填写正确的身份证信息", null);
//            return false;
//        }
//        return true;
//    }

    private void updateUserInfo(final User user) {
        String userJson = JsonUtil.Object2Json(user);
        HttpGo.post(NetUrl.UPDATE_USER_INFO)
                .params("userInfo", userJson)
                .execute(new HttpDialogListener(this) {
                    @Override
                    public void onSuccess(String s) {
//                        try {
                        User returnUser = JsonUtil.checkToGetData(s, User.class);
                        if (returnUser == null)
                            return;
//                            JSONObject obj = new JSONObject(s);
//                            User returnUser = null;
//                            String msg = "修改成功";
//                            if (obj.isNull("errCode") || "000000".equals(obj.getString("errCode"))) {
//                                msg = obj.getString("errDesc");
//                                DialogUtil.error(msg, obj.getString("errCode"), null);
//                            } else {
//                                if (!obj.isNull("data")) {
//                                    returnUser = new User(obj.optJSONObject("data"));
//                                }
//                                if (returnUser == null) {
//                                    msg = "修改失败";
//                                } else {
//                        initInfoDisplay(returnUser);
                        // MyApplication.getInstance().setUser(returnUser);
                        DataManager.getInstance().getUser().setAge(returnUser.getAge());
                        DataManager.getInstance().getUser().setBirthday(returnUser.getBirthday());
                        DataManager.getInstance().getUser().setCartNo(returnUser.getCartNo());
                        DataManager.getInstance().getUser().setRealName(returnUser.getRealName());
                        DataManager.getInstance().getUser().setSex(returnUser.getSex());
                        mUser = DataManager.getInstance().getUser();
                        initInfoDisplay(mUser);
//                        setModified(false, user);
//                        showModifyBtn();
//                                }
                    }
//                        } catch (JSONException e) {
//                            DialogUtil.confirm("修改失败", null);
//                            e.printStackTrace();
//                        }
//                }
                });
    }

    /**
     * 修改用户信息对象
     *
//     * @param user 需要修改的用户信息对象
     * @return 依据用户输入修改完毕的用户对象
     */
//    private User modifyUserInfo(User user) {
//        String realName = StringUtil.removeLineBreak(etName.getText().toString().trim());
////        if (!realName.contains("*")) {
////        if (Pattern.matches("[a-zA-Z.·\\p{Han}]{2,}", realName)) {
//        user.setRealName(realName);
////        } else {
////            DialogUtil.confirm(etName.getResources().getString(R.string.user_info_real_name_error_hint), null);
////            return null;
////        }
////        }
//        if (rbMale.isChecked()) {
//            user.setSex(SRConstant.SEX_BOY);
//        } else if (rbFemale.isChecked()) {
//            user.setSex(SRConstant.SEX_GIRL);
//        }
//        String age = StringUtil.removeLineBreak(etAge.getText().toString().trim());
////        if (age.matches("\\d+")) {
////        if (age.matches("^[1-9][0-9]?$")) {
//        user.setAge(age);
////        } else {
////            DialogUtil.confirm(R.string.age_input_wrong, null);
////            return null;
////        }
//        return user;
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
          if(resultCode != RESULT_OK){
             return;
          }
          if(requestCode == INPUT_NAME_REQUEST_CODE){
               String realName = data.getStringExtra(SRConstant.CONTENT_KEY);
               if(checkName(realName)){
                    mUser.setRealName(realName);
                    updateUserInfo(mUser);
               }else {
                   MyToast.show("输入名字有误！");
               }
          }
           if(requestCode == INPUT_AGE_REQUEST_CODE){
               String age = data.getStringExtra(SRConstant.CONTENT_KEY);
               if(checkAge(age)){
                   mUser.setAge(age);
                   updateUserInfo(mUser);
               }else {
                   MyToast.show("输入年龄有误！");
               }
           }
    }
}
