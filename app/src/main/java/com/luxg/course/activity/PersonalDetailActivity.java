package com.luxg.course.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.luxg.course.R;
import com.luxg.course.model.StudentInfo;
import com.luxg.course.util.ImageUtil;
import com.luxg.course.util.PreferenceUtil;
import com.luxg.course.widget.base.BezelImageView;
import com.luxg.course.widget.basewise.BaseWiseActivity;
import com.luxg.course.widget.basewise.Constant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by LuXiaogang on 2016/3/1.
 */
public class PersonalDetailActivity extends BaseWiseActivity implements View.OnClickListener {
    private BezelImageView personal_pic_head;
    private TextView tv_studentId, tv_studentName, tv_studentSex, tv_studentBirth, tv_submit;
    private EditText et_studentTel, et_studentClass;
    private PopupWindow mShowDialog;
    private Bitmap photo;
    private SharedPreferences userSpf;
    /**
     * headPath 头像图片的地址即图片的头
     */
    private String headPath = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_personal);
        setTitleView("我的资料");
        findView();
        userSpf = PreferenceUtil.getSharedPreferences(this);
        initView();
    }

    private void initView() {
        tv_studentId.setText(Constant.studentId);
        tv_studentName.setText(Constant.studentName);
        tv_studentSex.setText(Constant.studentSex == null ? "" : Constant.studentSex);
        tv_studentBirth.setText(Constant.studentBirth == null ? "" : Constant.studentBirth);
        et_studentTel.setText(Constant.studentTel == null ? "" : Constant.studentTel);
        et_studentClass.setText(Constant.studentClass == null ? "" : Constant.studentClass);
        /**
         * 需要显示头像
         */
        String str = PreferenceUtil.getSharedPreferences(getContext()).getString("avatar","");
        if (!TextUtils.isEmpty(str)) {
            ImageUtil.getInstance().displayImage(getContext(), personal_pic_head, str);
        }

    }

    private void findView() {
        personal_pic_head = (BezelImageView) findViewById(R.id.personal_pic_head);
        tv_studentId = (TextView) findViewById(R.id.tv_studentId);
        tv_studentName = (TextView) findViewById(R.id.tv_studentName);
        tv_studentSex = (TextView) findViewById(R.id.tv_studentSex);
        tv_studentBirth = (TextView) findViewById(R.id.tv_studentBirth);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        et_studentTel = (EditText) findViewById(R.id.et_studentTel);
        et_studentClass = (EditText) findViewById(R.id.et_studentClass);
        personal_pic_head.setOnClickListener(this);
        tv_studentSex.setOnClickListener(this);
        tv_studentBirth.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.personal_pic_head) {//点击头像
            showPop();
        } else if (v.getId() == R.id.tv_studentSex) {//点击性别
            tv_studentName.clearFocus();
            showSEXPop();
        } else if (v.getId() == R.id.tv_studentBirth) {
            // 生日
            tv_studentName.clearFocus();
            birthday();
        } else if (v.getId() == R.id.tv_submit) {
            //确定按钮
            submit();
        } else if (v.getId() == R.id.persinal_data_pop_camera) {
            pickGallery();
        } else if (v.getId() == R.id.persinal_data_pop_gallery) {
            takePic();
        } else if (v.getId() == R.id.persinal_data_pop_cancel) {
            mShowDialog.dismiss();
        } else if (v.getId() == R.id.persinal_data_pop_nan) {//设置性别男
            tv_studentSex.setText("男");
            mShowDialog.dismiss();
        } else if (v.getId() == R.id.persinal_data_pop_nv) {//设置性别女
            tv_studentSex.setText("女");
            mShowDialog.dismiss();
        }
    }

    /**
     * 确定按钮
     */
    private void submit() {
        final String sex = tv_studentSex.getText() == null ? "" : tv_studentSex.getText().toString();
        final String birth = tv_studentBirth.getText() == null ? "" : tv_studentBirth.getText().toString();
        final String tel = et_studentTel.getText() == null ? "" : et_studentTel.getText().toString();
        final String cla = et_studentClass.getText() == null ? "" : et_studentClass.getText().toString();
        if (sex.equals(Constant.studentSex == null ? "" : Constant.studentSex)
                && birth.equals(Constant.studentBirth == null ? "" : Constant.studentBirth)
                && tel.equals(Constant.studentTel == null ? "" : Constant.studentTel)
                && cla.equals(Constant.studentClass == null ? "" : Constant.studentClass)) {
            showToast("亲，您未做任何修改");
            return;
        }

        HttpUtils httpUtils = new HttpUtils();
        String Url = Constant.URL + "updateStudentByStuId?studentId=" + Constant.studentId
                + "&studentSex=" + sex
                + "&studentBirth=" + birth
                + "&studentTel=" + tel
                + "&studentClass=" + cla;
        httpUtils.send(HttpRequest.HttpMethod.POST,
                Url,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        /**
                         * 解析从后台取回的json数据
                         */
                        System.out.println("获取登录信息 : " + responseInfo.result.toString());
                        String jsonString = responseInfo.result;
                        Type type = new TypeToken<Boolean>() {
                        }.getType();
                        Gson json = new Gson();
                        boolean b = json.fromJson(jsonString, type);
                        /**
                         * 解析从后台取回的json数据
                         */
                        if (b) {
                            showToast("修改用户信息成功");
                            SharedPreferences.Editor editor = userSpf.edit();
                            Constant.studentBirth = birth;
                            Constant.studentClass = cla;//班级
                            Constant.studentTel = tel;
                            Constant.studentSex = sex;
                            editor.putString("studentBirth", Constant.studentBirth);
                            editor.putString("studentClass", Constant.studentClass);
                            editor.putString("studentTel", Constant.studentTel);
                            editor.putString("studentSex", Constant.studentSex);
                            editor.commit();

                            finish();
                        } else {
                            showToast("修改用户信息失败");
                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        showToast(msg);
                    }
                });
    }

    /**
     * 保存头像到服务器
     */
    private void savePicture(String path ) {
        showDialog();
        final String pimage = "/upload/" + path;
        final String image = "upload/" + path;//专门用来保存到sharedPreference
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();

        //定位地址
        if (!TextUtils.isEmpty(path)) {
            File file = new File(Environment.getExternalStorageDirectory(),path);
            params.addBodyParameter("studentId", Constant.studentId);
            params.addBodyParameter("imagePath", file);//用于保存到数据库的图片地址，即图片的头
            params.addBodyParameter("imagePath", pimage);
        }

        httpUtils.send(HttpRequest.HttpMethod.POST,
                Constant.URL + "changeStudentPicByStuId", params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        /**
                         * 解析从后台取回的json数据
                         */
                        System.out.println("获取回传信息 : " + responseInfo.result.toString());
                        String jsonString = responseInfo.result;
                        Type type = new TypeToken<Boolean>() {
                        }.getType();
                        Gson json = new Gson();
                        boolean b = json.fromJson(jsonString, type);
                        /**
                         * 解析从后台取回的json数据
                         */
                        if (b) {
                            SharedPreferences.Editor editor = userSpf.edit();
                            Constant.avatar = image == null ? "" : image;
                            editor.putString("avatar", Constant.avatar);
                            editor.commit();
                            showToast("更改头像成功");
                        } else {
                            showToast("更改头像失败");
                        }
                        dismissDialog();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        showToast(msg);
                    }
                });

    }

    /**
     * 选择生日
     */
    private void birthday() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(PersonalDetailActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear + 1;
                        tv_studentBirth.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                    }
                }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(new Date().getTime());
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date()); //设置时间为当前时间
        ca.add(Calendar.YEAR, -150); //年份减1
        dialog.getDatePicker().setMinDate(ca.getTimeInMillis());
        dialog.show();
    }

    /**
     * 显示照片来源填出框
     */
    private void showPop() {
        View view = LayoutInflater.from(this).inflate(R.layout.personal_data_show_pop, null);
        mShowDialog = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT, true);
        mShowDialog.setOutsideTouchable(true);

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mShowDialog.dismiss();
            }
        });

        view.findViewById(R.id.persinal_data_pop_camera).setOnClickListener(this);
        view.findViewById(R.id.persinal_data_pop_gallery).setOnClickListener(this);
        view.findViewById(R.id.persinal_data_pop_cancel).setOnClickListener(this);

        mShowDialog.showAtLocation(tv_submit, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 显示性别的选择框
     */
    private void showSEXPop() {
        View view = LayoutInflater.from(this).inflate(R.layout.personal_data_show_pop_sex, null);
        mShowDialog = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT, true);
        mShowDialog.setOutsideTouchable(true);

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mShowDialog.dismiss();
            }
        });

        view.findViewById(R.id.persinal_data_pop_nan).setOnClickListener(this);
        view.findViewById(R.id.persinal_data_pop_nv).setOnClickListener(this);
        view.findViewById(R.id.persinal_data_pop_cancel).setOnClickListener(this);

        mShowDialog.showAtLocation(tv_submit, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 拍照
     */
    private void takePic() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 1);
    }

    /**
     * 相册
     */
    private void pickGallery() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定调用相机拍照后的照片存储的路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(createTmpFile(PersonalDetailActivity.this)));
        startActivityForResult(intent, 2);
    }

    public static File createTmpFile(Context context) {

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 已挂载
            File pic = new File(context.getExternalCacheDir() + File.separator + "Pictures");
            if (!pic.exists()) {
                pic.mkdirs();
            }
            File tmpFile = new File(pic, "touxiang.jpg");
            return tmpFile;
        } else {
            File cacheDir = context.getCacheDir();
            File tmpFile = new File(cacheDir, "touxiang.jpg");
            return tmpFile;
        }

    }

    /**
     * 保存裁剪之后的图片数据
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            photo = extras.getParcelable("data");
            personal_pic_head.setImageBitmap(photo);
            headPath = saveMyBitmap(photo, "wql");
            savePicture(headPath );
        }
    }

    /**
     * 保存裁剪之后的图片数据 到本地sd卡
     */
    private String saveMyBitmap(Bitmap mBitmap, String bitName) {
        if (bitName.equals("wql")) {
            String uuidname = "wql";
            File f = new File(Environment.getExternalStorageDirectory(),
                    uuidname + ".jpg");// 保存到了sd卡中
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            try {
                fOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return uuidname + ".jpg";
        } else {
            String uuidname = UUID.randomUUID().toString();
            File f = new File(Environment.getExternalStorageDirectory(),
                    uuidname + ".jpg");// 保存到了sd卡中
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            try {
                fOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return uuidname + ".jpg";
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
        mShowDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 如果是直接从相册获取
                case 1:
                    if (data != null) {
                        startPhotoZoom(data.getData());
                        mShowDialog.dismiss();
                    } else {
                        mShowDialog.dismiss();
                    }
                    break;
                // 如果是调用相机拍照时
                case 2:
                    File temp = createTmpFile(PersonalDetailActivity.this);
                    startPhotoZoom(Uri.fromFile(temp));
                    mShowDialog.dismiss();
                    break;
                // 取得裁剪后的图片
                case 3:
                    if (data != null) {
                        setPicToView(data);
                        mShowDialog.dismiss();
                    }
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
