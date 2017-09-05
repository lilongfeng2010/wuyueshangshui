package com.wuyueshangshui.yuanxinkangfu.mainUI.fragment.mine;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.wuyueshangshui.yuanxinkangfu.EventBus.MineHeadImageBus;
import com.wuyueshangshui.yuanxinkangfu.R;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendBodyBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendHeadBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendOriginBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendRootBean;
import com.wuyueshangshui.yuanxinkangfu.common.Constant;
import com.wuyueshangshui.yuanxinkangfu.common.GlobalURL;
import com.wuyueshangshui.yuanxinkangfu.login.LoginMainActivity;
import com.wuyueshangshui.yuanxinkangfu.mainactivity.BaseActivity;
import com.wuyueshangshui.yuanxinkangfu.mainactivity.MyApplication;
import com.wuyueshangshui.yuanxinkangfu.utils.Images.ImageUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.Images.URIUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.LogUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.Net.MyCallBack;
import com.wuyueshangshui.yuanxinkangfu.utils.SPUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.dialog.ToastUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.encrypt.DES;
import com.wuyueshangshui.yuanxinkangfu.wigdet.ActivityCollector;
import com.wuyueshangshui.yuanxinkangfu.wigdet.ListItem2;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.MediaType;

/**
 * 个人信息界面
 */
public class PersonInformationActivity extends BaseActivity implements View.OnClickListener {

    private ListItem2 identify_cardItem;
    private ListItem2 mobileItem;
    private ListItem2 nameItem;
    private ListItem2 professionItem;

    private TextView user_id;
    private TextView identify_card;
    private CircleImageView head_image;
    private TextView mobile;
    private TextView name;
    private Intent allIntent;

    private TextView profession;
    private ListItem2 passwordItem;
    private PopupWindow mPopupWindow;
    private View headImageItem;
    private String imagePath;
    private LinearLayout exit;
    private ListItem2 bind_wechatItem;
    private TextView wechat_name;
    private TextView sex;
    private TextView age;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_information);
        //初始化控件
        initView();
        //初始化数据
        initData();
        //监听
        initListener();
        //设置性别和年龄
        setAgeAndSex();
    }

    private void initView() {
        findViewById(R.id.all_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.all_title_text)).setText("个人信息");
        //用户头像
        headImageItem = findViewById(R.id.person_information_headItem);
        head_image = (CircleImageView) headImageItem.findViewById(R.id.listitem3_image);
        //用户ID
        user_id = (TextView)findViewById(R.id.person_information_ID).findViewById(R.id.list_item2_content);
        //手机号
        mobileItem = (ListItem2) findViewById(R.id.person_information_mobile);
        mobile = (TextView)mobileItem.findViewById(R.id.list_item2_content);
        //用户姓名
        nameItem = (ListItem2) findViewById(R.id.person_information_name);
        name = (TextView) nameItem.findViewById(R.id.list_item2_content);
        //用户身份证
        identify_cardItem = (ListItem2) findViewById(R.id.identify_card);
        identify_card = (TextView)identify_cardItem.findViewById(R.id.list_item2_content);
        //用户性别
        sex = (TextView) findViewById(R.id.sex_setting).findViewById(R.id.list_item2_content);
        //用户年龄
        age = (TextView) findViewById(R.id.age_setting).findViewById(R.id.list_item2_content);
        //用户职业
        professionItem = (ListItem2) findViewById(R.id.profession_Setting);
        profession = (TextView) professionItem.findViewById(R.id.list_item2_content);
        //绑定微信
        bind_wechatItem = (ListItem2) findViewById(R.id.bind_wechat_setting);
        wechat_name = (TextView) bind_wechatItem.findViewById(R.id.list_item2_content);
        //密码管理
        passwordItem = (ListItem2) findViewById(R.id.password_setting);
        //退出
        exit = (LinearLayout) findViewById(R.id.exit_button);
    }

    private void initData() {
        //显示头像
        Picasso.with(this)
                .load(SPUtils.getString(this, Constant.wechat_headimg_url,null))
                .error(R.color.graybackground)
                .placeholder(R.color.graybackground)
                .into(head_image);
        user_id.setText(SPUtils.getString(this,Constant.user_id,null));
        mobile.setText(SPUtils.getString(this,Constant.mobile,null));
        name.setText(SPUtils.getString(this,Constant.real_name,null));
        identify_card.setText(SPUtils.getString(this,Constant.identify_card_num,null));
        profession.setText(SPUtils.getString(this,Constant.job,null));
    }
    private void initListener() {
        headImageItem.setOnClickListener(this);
        mobileItem.setOnClickListener(this);//点击手机号的条目
        nameItem.setOnClickListener(this);//点击姓名的条目
        identify_cardItem.setOnClickListener(this);//点击身份证的条目
        professionItem.setOnClickListener(this);//点击职业的条目
        bind_wechatItem.setOnClickListener(this);
        passwordItem.setOnClickListener(this);//点击修改密码的条目
        exit.setOnClickListener(this);//点击退出登录
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.all_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.person_information_headItem:
                showPopupWindow();
                break;
            case R.id.person_information_mobile:
                //跳转到设置手机号的页面
                allIntent =new Intent(this,SettingPhoneActivity.class);
                startActivityForResult(allIntent,Constant.PERSON_TO_SETPHONENUM_TAG);
                StartAnimation();
                break;
            case R.id.person_information_name:
                //跳转到姓名设置页面
                allIntent=new Intent(this,SettingNameActivity.class);
                if (!TextUtils.isEmpty(name.getText().toString())){
                    allIntent.putExtra(Constant.name,name.getText().toString());
                }
                startActivityForResult(allIntent,Constant.PERSON_TO_CHANGE_NAME_TAG);
                StartAnimation();
                break;
            case R.id.identify_card:
                //跳转到身份证号设置页面
                allIntent=new Intent(this,SettingIdentifyCardActivity.class);
                if (!TextUtils.isEmpty(identify_card.getText().toString())){
                    allIntent.putExtra(Constant.identify_card_num,identify_card.getText().toString());
                }
                startActivityForResult(allIntent,Constant.PERSON_TO_CHANGE_IDENTIFYNUM_TAG);
                StartAnimation();
                break;
            case R.id.profession_Setting:
                //跳转到职业设置的页面
                allIntent=new Intent(this,SettingProfessionActivity.class);
                //如果已经有职业，进入职业选择页面也要显示出来
                if (!TextUtils.isEmpty(profession.getText().toString())){
                    allIntent.putExtra(Constant.job,profession.getText().toString());
                }
                startActivityForResult(allIntent,Constant.PERSON_TO_PROFESSION_TAG);
                StartAnimation();
                break;
            case R.id.bind_wechat_setting:
                //绑定微信
                if (!MyApplication.api.isWXAppInstalled()){
                    Toast.makeText(this, "您还未安装微信客户端", Toast.LENGTH_SHORT).show();
                    return;
                }
                final SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "wuyueshangshui";
                MyApplication.api.sendReq(req);
                break;
            case R.id.password_setting:
                allIntent=new Intent(this,SettingPasswordActivity.class);
                startActivity(allIntent);
                StartAnimation();
                break;
            case R.id.exit_button://退出登录
                SPUtils.clearAllData(this);
                startActivity(new Intent(this, LoginMainActivity.class));
                ActivityCollector.finishAll();
                break;
        }
    }

    private void showPopupWindow() {
        View contentView= LayoutInflater.from(this).inflate(R.layout.popupwindow,null);
        mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        mPopupWindow.setContentView(contentView);
        LinearLayout photo= (LinearLayout) contentView.findViewById(R.id.popup_takephoto);
        LinearLayout album= (LinearLayout) contentView.findViewById(R.id.popup_album);
        TextView cancel= (TextView) contentView.findViewById(R.id.popup_cancel);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                StartTakePhoto();
            }
        });
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                StartAblum();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        View rootView=LayoutInflater.from(PersonInformationActivity.this).inflate(R.layout.activity_person_information,null);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setAnimationStyle(R.style.popup_animation);
        mPopupWindow.showAtLocation(rootView,Gravity.BOTTOM,0,0);
    }
    private Uri imageUri;
    private static final int TAKE_PHOTO=100;
    private static final int ABLUM=101;
    private void StartAblum() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        }else{
            openAblum();
        }
    }

    private void openAblum() {
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,ABLUM);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAblum();
                }else{
                    ToastUtils.Toast(this,"您拒绝了打开相册");
                }
                break;
            default:
        }
    }

    private void StartTakePhoto() {
        //创建file对象，用于存储拍照后的照片
        File outputImage=new File(getExternalCacheDir(),"output_image.jpg");
        try {
            if (outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT>=24){
            imageUri= FileProvider.getUriForFile(this,Constant.outputImage_uri,outputImage);
        }else{
            imageUri=Uri.fromFile(outputImage);
        }
        //启动相机程序
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,TAKE_PHOTO);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Constant.PERSON_TO_SETPHONENUM_TAG && resultCode==Constant.SETPHONENUM_TO_PERSON_TAG){
            //修改电话
            if (data!=null){
                mobile.setText(data.getStringExtra(Constant.phone));
            }
        }else if (requestCode==Constant.PERSON_TO_CHANGE_NAME_TAG && resultCode==Constant.CHANGE_NAME_TO_PERSON_TAG){
            //修改姓名
            if (data!=null){
                name.setText(data.getStringExtra(Constant.name));
            }
        }else if(requestCode==Constant.PERSON_TO_CHANGE_IDENTIFYNUM_TAG && resultCode==Constant.CHANGE_IDENTIFYNUM_TO_PERSON_TAG){
            //修改身份证号
            if (data!=null){
                LogUtils.showe("====回传接收的的数据==="+data.getStringExtra(Constant.identify_card_num));
                identify_card.setText(data.getStringExtra(Constant.identify_card_num));

                setAgeAndSex();


            }
        }else if (requestCode==Constant.PERSON_TO_PROFESSION_TAG && resultCode==Constant.PROFESSION_TO_PERSON_TAG){
            //修改职业
            if (data!=null){
                profession.setText(data.getStringExtra(Constant.job));
            }
        }else if (requestCode==TAKE_PHOTO && resultCode==RESULT_OK){
//            try {
//                Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
//                head_image.setImageBitmap(bitmap);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            uploadHeadImage(imageUri.getPath());
//            uploadHeadImage(URIUtils.getRealFilePath(this,imageUri));
//            uploadHeadImage(URIUtils.getImageAbsolutePath(this,imageUri));
        }else if(requestCode==ABLUM && resultCode==RESULT_OK){
            //判断手机系统版本号
            if (Build.VERSION.SDK_INT>=19){
                //4.4及以上系统使用这个方法处理图片
                handleImageOnKitKat(data);
            }else{
                //4.4以下的系统使用这个方法处理图片
                handleImageBeforeKitkat(data);
            }
        }

    }

    private void setAgeAndSex() {
        switch (SPUtils.getString(this,Constant.gender,"0")){
            case "0":
                sex.setText("未知");
                break;
            case "1":
                sex.setText("男");
                break;
            case "2":
                sex.setText("女");
                break;
        }
        int userBirthyear=Integer.parseInt(SPUtils.getString(this,Constant.birthday,"1992-03-03").substring(0,4));
        LogUtils.showe("==出生年份===="+userBirthyear+"::"+Calendar.getInstance().get(Calendar.YEAR));
        age.setText(Calendar.getInstance().get(Calendar.YEAR)-userBirthyear+"");
    }

    private void handleImageBeforeKitkat(Intent data) {
        Uri uri=data.getData();
        imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        Uri uri=data.getData();
        if (DocumentsContract.isDocumentUri(this,uri)){
            //如果是document类型的uri，则通过document的id处理
            String docId=DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id=docId.split(":")[1];//解析出数字格式的id
                String selection=MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);

            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }

        }else if("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的uri，则使用普通的方式处理
            imagePath=getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath=uri.getPath();
        }
        displayImage(imagePath);
    }

    private void displayImage(String imagePath) {
        if (imagePath!=null){
//            Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
//            head_image.setImageBitmap(bitmap);
            //上传头像图片
            uploadHeadImage(imagePath);
        }else{
            ToastUtils.Toast(this,"获取图片失败");
        }
    }

    private void uploadHeadImage(String imagePath) {
       ToastUtils.showLoadingToast(this);
        String encryptData= DES.encrypt(gson.toJson(SendHeadImageBean(imagePath)));
        OkHttpUtils.postString().url(GlobalURL.BaseURL)
                .content(encryptData)
                .mediaType(MediaType.parse("application/json;charset=utf-8"))
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.showe("=====设置头像的返回值fail======"+e);
                        ToastUtils.dismissLoadingToast();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        String decryptData=DES.decrypt(response);
                        LogUtils.showe("=====设置头像的返回值success======"+decryptData);
                        returnData(decryptData);

                    }
                });
    }

    private void returnData(String decryptData) {
        try {
            JSONObject origin = new JSONObject(decryptData);
            JSONObject root=origin.optJSONObject(Constant.root);
            JSONObject head=root.optJSONObject(Constant.head);
            ToastUtils.dismissLoadingToast();
            if (head.optInt(Constant.code)==0){
                ToastUtils.Toast(this,"修改成功");
                head_image.setImageURI(Uri.fromFile(new File(imagePath)));
                SPUtils.setString(this,Constant.wechat_headimg_url,root.optJSONObject(Constant.body).optString(Constant.avatar));
                EventBus.getDefault().post(new MineHeadImageBus(imagePath));
            }else{
                ToastUtils.Toast(this,head.optString(Constant.text));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Object SendHeadImageBean(String imagePath) {
        String smallImageData=ImageUtils.bitmapToString(imagePath);
        SendOriginBean origin=new SendOriginBean();
        SendRootBean root=new SendRootBean();
        SendHeadBean head=new SendHeadBean();
        SendBodyBean body=new SendBodyBean();
        head.setClientid(Constant.clientid);
        head.setAction(117);
        head.setTimestamp(System.currentTimeMillis()+"");
        body.setUser_id(SPUtils.getString(this,Constant.user_id,null));
        body.setType(7);
        body.setAvatar(smallImageData);
        root.setHead(head);
        root.setBody(body);
        origin.setRoot(root);
        return origin;
    }

    private String getImagePath(Uri uri,String selection){
        String path=null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if (cursor!=null){
            if (cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    public void StartAnimation(){
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
