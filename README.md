Error Reporting SDK
==========
Error Reporting SDK(이하 ERS)는 Android 앱에서 발생하는 Crash나 개발자가 기록하고자 하는 Log를 서버에 보고할 수 있게 해주는 라이브러리입니다.  
Android API 16버전부터 지원합니다.

ERS는 다음과 같은 특징이 있습니다.
* 통신 라이브러리로 [http-client-lib](https://oss.navercorp.com/da-intern-2019-1h/Http-Client-Lib/tree/develop)을 사용했습니다.
* Crash(비정상 종료)가 발생할 경우 [서버](https://oss.navercorp.com/da-intern-2019-1h/Error-Reporting-Server) 쪽에 Error Report를 전송합니다.
* Crash 뿐만 아니라 Log Level 등의 설정을 통해 사용자 정의된 오류 및 로그도 수집할 수 있습니다.
* 통신상태가 원활하지 않을 경우 Device의 Local Storage에 저장 했다가, 통신 가능할 때에 서버로 전송합니다.
    * Local Storage는 안드로이드에서 제공하는 [Room](https://developer.android.com/jetpack/androidx/releases/room)을 사용했습니다.
* Error Report 수집시 사용자 식별 정보들을 같이 전송합니다.
    * Time
    * Android ID
    * App Version
    * Android SDK Version
    * Package Name
    * Log Level
    * Tag
    * Message
    * StackTrace
    * Available Memory
    * Total Memory
* 사용자는 이외에도 추가적인 key-value 형식으로 Custom Data를 구성하여 함께 기록할 수 있습니다.

Dependency 추가
======

### Gradle
프로젝트 루트 수준의 build.gradle에 Maven URL을 추가해주세요.

```groovy
allprojects {
    repositories {
        maven { url "https://dl.bintray.com/naver/Ers" }
    }
}
```

그리고 앱 수준의 build.gradle에 다음과 같이 dependency를 추가해주세요.

```groovy
dependencies {
    implementation 'com.naver.ers:ErrorReportingSdk:0.1.0'
}
```

### Maven
다음과 같이 Repository와 Dependency를 지정해주세요.

```xml
<repositories>
  ...
  <repository>
    <id>HttpClientLib</id>
    <url>https://dl.bintray.com/naver/Ers</url>
  </repository>
</repositories>

<dependencies>
  ...
  <dependency>
    <groupId>com.naver.ersb</groupId>
    <artifactId>ErrorReportingSdk</artifactId>
    <version>0.1.0</version>
    <type>pom</type>
  </dependency>
</dependencies>
```

Manual
=====
ERS을 사용하는 방법은 간단합니다.
어플리케이션 클래스 내에서 Reporter 클래스에서 static method로 제공하는 register()를 통해 Application Object를 등록해주기만 하면 됩니다.
예제는 다음과 같습니다.
```java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Reporter.register(this);
    }
}
```
### 주의사항
* Application을 등록해야합니다. 다른 안드로이드 컴포넌트는 등록할 수 없습니다.
* Reporter에 등록한 Application class는 반드시 AndroidManifest.xml에 추가해야 합니다.

Custom Data
=====
사용자는 key-value 형식의 Custom Data를 구성할 수 있습니다.
ERS는 이를 위해 사용자에게 CustomData.Builder를 제공하고 있습니다.
CustomData.Builder를 통해 구성이 완료된 데이터는 build() 메소드를 통해 CustomData로 만들고, 이를 Reporter 클래스에 setCustomData() 메소드로 등록하면 이후 Crash나 Logging 발생시 사용자가 등록한 CustomData가 함께 전송됩니다.
예제는 다음과 같습니다.
```java
public class TestApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        CustomData.Builder customDataBuilder = new CustomData.Builder()
                .putData("test_str", "ReadyKim")
                .putData("test_num", 26);
        Reporter.setCustomData(customDataBuilder.build());
        Reporter.register(this);
    }
}
```
_이때 CustomData의 등록은 반드시 Application 클래스에서 이루어질 필요도, register()보다 먼저 이루어질 필요도 없습니다._
