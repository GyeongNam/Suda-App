## 소개 및 사이트 링크
 
!!생애 첫 App 개발!!

실시간 채팅이 가능한 커뮤니티 앱


![ic_launcher_sudas](https://user-images.githubusercontent.com/63902992/143762908-caf2ddc3-eaab-4728-8a4c-91ab70142db3.png)


## 개발 인원

- [GyeongNam](https://github.com/GyeongNam/)
- [dorumamu](https://github.com/dorumamu)
- [tmdwlsla123](https://github.com/tmdwlsla123)

## 개발 환경
Client
- 운영체제:   Android 10 <br>
- 웹 서버:    Apache 2.2.15<br>
- DB:         Room DB<br>

[Server](https://github.com/GyeongNam/SUDA_SERVER)
- 운영체제:   Linux (CentOS 6.10)<br>
- DB:         MySQL 5.1.73<br>
- framework:  Laravel 7.10.3<br>
  
## 시스템 구성도
![image](https://user-images.githubusercontent.com/63902992/143757515-bfefa751-6721-47cc-a0c4-1df18e5332f2.png)

## 통신 프로세스
![image](https://user-images.githubusercontent.com/63902992/143760299-6e8b59e8-4c75-4dad-bc08-16ea5226bbc8.png)


## GyeongNam

해당 목록은 '경남' 이 만든 것만 설명되어있습니다.

0. 기획
  - 스토리보드 생성
1. 설정과 FCM

    <img src="https://user-images.githubusercontent.com/63902992/143763183-a30ec0ba-76ce-4cc8-9fef-c1ff5dbd160a.jpg" width="50%" height="50%"/>
  
  - Client

    FirebaseMessagingService를 상속받는 클래스 생성
    ```java
        public class MyFirebaseMessagingService extends FirebaseMessagingService { ... }
    ```

    ```java
        public void onMessageReceived(RemoteMessage remoteMessage) {
          super.onMessageReceived(remoteMessage);

          String title = remoteMessage.getData().get("title");
          String message = remoteMessage.getData().get("body");
          String key = remoteMessage.getData().get("key");
          String num = remoteMessage.getData().get("num");


          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

              String channel = "채널";
              String channel_nm = "채널명";


              NotificationManager notichannel = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
              NotificationChannel channelMessage = new NotificationChannel(channel, channel_nm,
                      NotificationManager.IMPORTANCE_DEFAULT);
              channelMessage.setDescription("채널에 대한 설명.");
              channelMessage.enableLights(true);
              channelMessage.enableVibration(true);
              channelMessage.setShowBadge(false);
              channelMessage.setVibrationPattern(new long[]{1000, 1000});
              notichannel.createNotificationChannel(channelMessage);
              Intent nointent = new Intent(this, boardActivity.class);
              nointent.setAction(Intent.ACTION_MAIN);
              nointent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
              nointent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              nointent.putExtra("room",num);  // 클릭시 넘길 데이터

              PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, nointent,
                      PendingIntent.FLAG_UPDATE_CURRENT);


              //푸시알림을 Builder를 이용하여 만듭니다.
              NotificationCompat.Builder notificationBuilder =
                      new NotificationCompat.Builder(this, channel)
                              .setSmallIcon(R.drawable.sudaicon)
                              .setContentTitle(title)//푸시알림의 제목
                              .setContentText(message)//푸시알림의 내용
                              .setChannelId(channel)
                              .setAutoCancel(true)//선택시 자동으로 삭제되도록 설정.
                              .setContentIntent(pendingIntent)//알림을 눌렀을때 실행할 인텐트 설정.
                              .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

              NotificationManager notificationManager =
                      (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

              notificationManager.notify(9999, notificationBuilder.build());
          }
    ```
    해당 내용을 보인이 설정한 키워드를 포함한 게시글과 댓글이 생겼을 경우, 자동으로 FCM 알림을 보내준다.
    채팅방에 메시지가 왔을때도 보내준다.
  
  - Server

    .env에 FCM key와 아이디를 연결한다.
    컨트롤러에서 FCM 호출
    ```php
    FCMController::fcm($user, $message, $talktoken, "2", $room);
    ```
    ```php
    static public function fcm($title, $body, $token, $key, $num){
        $tokens = array();

        if(sizeof($token) > 0 ){
            foreach ($token as $Rresult) {
                $tokens[] = $Rresult->Token;
            }
        }

        $myMessage = "새글이 등록되었습니다.";

        $message = array(
          'title' => $title,
          'body' => $body,
          'key' => $key,
          'num' => $num
        );

        $url = 'https://fcm.googleapis.com/fcm/send';
        $fields = array(
                    'registration_ids' => $tokens,
                    'data' => $message
                );

        $headers = array(
                'Authorization:key =' 키값 '
                ,'Content-Type:application/json'
                );

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt ($ch, CURLOPT_SSL_VERIFYHOST, 0);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
        // return $fields;
        $result = curl_exec($ch);
        if ($result === FALSE) {
            die('Curl failed: ' . curl_error($ch));
        }
        curl_close($ch);

        echo $result;
    }
    ```

2. 채팅

  ![image](https://user-images.githubusercontent.com/63902992/143763884-84f6bfca-d119-4930-bdbc-ff66d3b73d9a.png)
  
  ![image](https://user-images.githubusercontent.com/63902992/143763909-4f1d84b9-afbd-4585-88c5-d29b08ce19e8.png)
  
  - Client
    
    <img src="https://user-images.githubusercontent.com/63902992/143763959-25c3217d-59ff-495d-be38-10d8531a1a20.jpg" width="25%" height="25%"/><img src="https://user-images.githubusercontent.com/63902992/143763972-8446aa99-5930-4160-8f89-f594d3cc46bc.jpg" width="25%" height="25%"/><img src="https://user-images.githubusercontent.com/63902992/143763979-81cc3fd9-94d9-4102-92b4-be385aeec915.jpg" width="25%" height="25%"/><img src="https://user-images.githubusercontent.com/63902992/143763983-268f03db-6db7-4609-a3cb-59bf9f6ea9b1.jpg" width="25%" height="25%"/>

    1. 우측하단 + 클릭
    2. 채팅방 이름과 인원 선택
    3. 텍스트 입력 및 사진 전송
    4. 우측상단 메뉴로 채팅방 인원 확인 및 채팅방 나가기 기능

  텍스트 전송
  ```java
  public void talkRequest() {
      String url = "chartEvent";
      api = HttpClient.getRetrofit().create( ApiInterface.class );
      HashMap<String,String> params = new HashMap<>();
      String other = "test";
      params.put("sendmsg", msgcheck);
      params.put("user", userinfo);
      params.put("room", String.valueOf(room));

      Call<String> call = api.requestPost(url,params);
  }
  ```
  이미지 전송
  ```java
  public void sendimg(String imgPath) {
    String url = "sendimg"; 
    api = HttpClient.getRetrofit().create(ApiInterface.class);
    HashMap<String, String> params = new HashMap<>();
    String other = "test";

    params.put("user", userinfo);
    params.put("room", String.valueOf(room));
    
    //이미지 파일 추가
    MultipartBody.Part filepart = null;
    if (imgPath != null) {
        File file = new File(imgPath);
        byte[] dd = UtilClass.getStreamByteFromImage(file);
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(imgPath),
                        dd
                );
        filepart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
    }
    Call<String> call = api.requestFilePost(url, params, filepart);
  }
  ```
  
  실시간 DB 업데이트
  ```java
  echo = new SocketIOConnector(options);

  echo.connect(new EchoCallback() {
      @Override
      public void call(Object... args) {
          Log.d("Success", String.valueOf(args));
      }
  }, new EchoCallback() {
      @Override
      public void call(Object... args) {
          Log.d("Error", String.valueOf(args));
      }
  });
  for (int i = 0; i < talkDatabase.talkDao().get_room_number(userinfo).size(); i++) {
      String room_number = talkDatabase.talkDao().get_room_number(userinfo).get(i).getRoom_number();
      array.add(room_number);
      echo.channel("laravel_database_" + room_number)
              .listen("chartEvent", new EchoCallback() {
                  @Override
                  public void call(Object... args) {
                      String user;
                      String message;
                      String chat_idx;
                      String time;
                      String user_count;
                      String image_status;
                      String image_uri;
                      int channel;
                      try {
                          Intent intent = new Intent("msg" + room_number);

                          JSONObject jsonObject = new JSONObject(args[1].toString());
                          image_status = jsonObject.getString("image_status");

                          user = jsonObject.getString("user");
                          message = jsonObject.getString("message");
                          image_uri = jsonObject.getString("message");
                          channel = Integer.parseInt(jsonObject.getString("channel"));
                          chat_idx = jsonObject.getString("chat_idx");
                          time = jsonObject.getString("time");
                          user_count = jsonObject.getString("user_count");
                          if(image_status.equals("1")){
                              message = "사진을 보냈습니다.";
                          }
                          Talk t = new Talk(null, user, message, channel, time, chat_idx, "0", user_count, image_status,image_uri);
                          Log.v("1", String.valueOf(t));
                          talkDatabase.talkDao().insert(t);
                          intent.putExtra("user", user);
                          intent.putExtra("message", message);
                          intent.putExtra("channel", String.valueOf(channel));
                          intent.putExtra("chat_idx", chat_idx);
                          intent.putExtra("time", time);
                          intent.putExtra("image_status",image_status);
                          LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                      } catch (JSONException e) {
                          e.printStackTrace();
                      }
                  }
              });
  }
  ```
  DB 라이브데이터 보여주기
  ```java
  viewModel = new ViewModelProvider(this).get(User_listViewModel.class);
  viewModel.get_Talk_listViewModel(room).observe(this, new Observer<List<Talk>>() {
      @Override
      public void onChanged(List<Talk> talks) {
          chatListAdapter.getTalkList(talks);
          try {
              if(flag==null){
                  flag = "1";
                  recyclerView.scrollToPosition(0);
                  recyclerView.getLayoutManager().scrollToPosition(0);
              }
              else{
                  if(chatListAdapter.list_itemArraylist.get(0).getUser().equals(userinfo)){
                      recyclerView.scrollToPosition(0);
                  }
              }
          }catch (Exception e){

          }
      }
  });
  ```

  - Server

  텍스트 전송
  ```php
  function chatting(Request $request){
    $date = new DateTime();
    $user = $request->user;
    $room = $request->room;
    $message = $request->sendmsg;
    $user_count = DB::table('chat_room')->where('chat_room',$room)->get()->count()-1;
    $chatidx = DB::table('chat_list')->insertGetId([
      'message' => $message,
      'user' => $user,
      'ch_idx' => $room,
      'created_at' =>$date->format('Y-m-d H:i:s'),
      'chat_status' => $user_count
    ]);
    DB::table('chat_room')->where('user',$user)->where('chat_room',$room)->update([
      'lately_chat_idx' => $chatidx
    ]);
    $talktoken =
    DB::table('users')->
    select('Token')->
    join('chat_room', 'chat_room.user','=', 'users.id')->
    where('chat_room.user', '!=', $user)->
    where('chat_room.chat_room','=',$room)->
    get();
    FCMController::fcm($user, $message, $talktoken, "2", $room);    // FCM 알림보내기
    broadcast(new \App\Events\chartEvent($request->user, $request->room, $request->sendmsg,null,$chatidx,$date->format('Y-m-d H:i:s'),$user_count,null)); // 웹소켓으로 실시간 데이터 보내기
    return $talktoken;
  }
  ```
  
  이미지 전송
  ```php
  function sendimg(Request $request) {

    if($request->hasFile('image')){
      $image = $request->file('image');
      $picture= $image->getClientOriginalName();
      Image::make($image)->save(public_path('/img/'.$picture));
    }
    else {
      $picture = null;
    }
      $date = new DateTime();
      $user = $request->user;
      $room = $request->room;
      $user_count = DB::table('chat_room')->where('chat_room',$room)->get()->count()-1;
      $image_status = 1;
      $message = "사진";
      $chatidx = DB::table('chat_list')->insertGetId([
       'img' => $picture,
       'user' => $user,
       'ch_idx' => $room,
       'message' => $message,
       'created_at' =>$date->format('Y-m-d H:i:s'),
       'chat_status' => $user_count
      ]);

      DB::table('chat_room')->where('user',$user)->where('chat_room',$room)->update([
       'lately_chat_idx' => $chatidx
      ]);

      $talktoken =
      DB::table('users')->
      select('Token')->
      join('chat_room', 'chat_room.user','=', 'users.id')->
      where('chat_room.user', '!=', $user)->
      where('chat_room.chat_room','=',$room)->
      get();
      FCMController::fcm($user, $message, $talktoken, "2", $room);  // FCM 알림 보내기
      broadcast(new \App\Events\chartEvent($user, $room, $picture,null,$chatidx,$date->format('Y-m-d H:i:s'),$user_count,$image_status)); // 웹소켓으로 실시간 데이터 
      return $talktoken;
 }
  ```
  


## dorumamu
## tmdwlsla123
