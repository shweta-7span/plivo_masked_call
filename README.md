# Plivo Call for Android

Reference from [Plivo Voice Quickstart for Android](https://github.com/plivo/plivo-android-quickstart-app)

### Introduction

To get started with this application follow these steps.
- **Outgoing Call:** Follow the below steps to start making outbound calls
    - [Install the Plivo Android SDK using gradle](#bullet1)
    - [Buy Number](#bullet2)
    - [Create Endpoints](#bullet3)
    - [Change "Direct Dial" XML Applications](#bullet4)
    - [Register and Unregister Endpoint](#bullet5)
    - [Run the app](#bullet6)
    - [Making an outgoing call](#bullet7)


### <a name="bullet1"></a> Install the Plivo Android SDK using gradle

Simply add the following to your build.gradle under app folder
```
// Replace 2.0.16 with latest version
implementation 'com.plivo.endpoint:endpoint:2.0.16@aar'
```
If you don't want to use gradle, simply build project since we already have aar file by default under plivo-android-quickstart-app/app/libs. 

[SDK Reference](https://www.plivo.com/docs/sdk/client/android/reference) - More documentation related to the Voice Android SDK


### Sign up for an account
Register [here](https://console.plivo.com/accounts/register/) to get your free Plivo account today.

Below are the steps that are to be followed to successfully Sign up for a free trial account.
1. Sign up with your corporate email address
2. You will receive an activation email from Plivo. Click on the link mentioned in the email to activate your account.
3. Enter an sms enabled, non-VOIP number to complete the phone verification.

Sign up with your corporate email address

![signup](https://github.com/shweta-7span/plivo_masked_call/assets/109585734/1a1b62e8-7e08-4ede-9cfc-075a4bb346f2)

If you are facing any issues while creating a Plivo account, you can reach out to our [Support Team](https://support.plivo.com/support/home)

### <a name="bullet2"></a> Buy Number
In Plivo, you can purchase virtual phone numbers that will act as the masked numbers for your calls. You can obtain these virtual numbers through the Plivo Dashboard.

<img width="1427" alt="buy_number" src="https://github.com/shweta-7span/plivo_masked_call/assets/109585734/25afd495-ab1b-46c6-8533-092a511addfc">


### <a name="bullet3"></a> Create Endpoints

You can create an endpoint from the Plivo Console and assign an application to make and receive calls after configuring the endpoint in the quickstart app.

<img width="1427" alt="endpoints" src="https://github.com/shweta-7span/plivo_masked_call/assets/109585734/e23a7f29-622e-4153-8915-9f6d4f43b649">

**Note:** You can make use of the demo 'Direct Dial' app in your account for the endpoint which will allow you to make and receive calls for testing purposes. In the above image it is 'Direct_Dial' as I update the name of it.

### <a name="bullet4"></a> Change "Direct Dial" XML Applications
Put the purchased number instead of `PUT YOUR CALLER ID HERE` in both 'Primary Answer URL' and 'Hangup URL'. To check more about this visit [here](https://support.plivo.com/hc/en-us/articles/360041854511-How-can-I-make-an-outbound-call-using-an-endpoint-).

<img width="1427" alt="xml_app_1" src="https://github.com/shweta-7span/plivo_masked_call/assets/109585734/1d10d8d6-45e5-41d9-8096-c8f0ef815a83">

### <a name="bullet5"></a> Register and Unregister Endpoints

Implement SIP register to Plivo Communication Server

To register with Plivo's SIP and Media server , use a valid sip uri account from plivo web console 
```

//Instantiate the Endpoint clas
private Endpoint endpoint;
Endpoint endpoint = Endpoint.newInstance(true, this);


//Instantiate the Endpoint clas with options
public static HashMap<String, Object> options = new HashMap<String, Object>()
{
    {
        put("debug",true);
        put("enableTracking",true);
        put("maxAverageBitrate", 21000);
    }
};

Endpoint endpoint = Endpoint.newInstance(true, this, options);


// To register with SIP Server
public void login(String username, String password) {
   endpoint.login(username, password);
}

// To register with SIP Server
public void login(String username, String password) {
   endpoint.login(username, password);
}

// To register with SIP Server using device token for getting Push Notifications
public void login(String username, String password, String fcmToken) {
   endpoint.login(username, password, fcmToken);
}

// To register with SIP Server using device token and certificate id
public void login(String username, String password, String fcmToken, String certificateId) {
   endpoint.login(username, password, fcmToken, certificateId);
}

// To register with SIP Server using registration timeout
public void login(String username, String password, String regTimeout) {
   endpoint.login(username, password, regTimeout);
}

//To unregister with SIP Server
public void logout() {
   endpoint.logout();
}
```

If the registration to an endpoint succeeds the following delegate gets called 
```
@Override
public void onLogin() {
   if (loginListener != null) loginListener.onLogin(true);
}
```

If the registration to an endpoint fails the following delegate gets called 
```
@Override
public void onLoginFailed() {
   if (loginListener != null) loginListener.onLogin(false);
}
```


### <a name="bullet6"></a> Run the app
   - Clone the [repo](https://github.com/shweta-7span/plivo_masked_call) and open plivo_masked_call.
   - Change sip endpoint username and password in [Utils](https://github.com/shweta-7span/plivo_masked_call/blob/main/app/src/main/java/com/example/demoplivo/Utils.java).
   - Build and run the app.  
   - After successful login make VoiceCalls.


### <a name="bullet7"></a> Making an outgoing call

###### Make an outgoing call with destination & headers:
Create PlivoOutgoingCall object , then make a call with destination and headers 
```
public Outgoing getOutgoing() {
    return endpoint.createOutgoingCall();
}
    
private void makeCall(String phoneNum) {
    Outgoing outgoing = ((App) getApplication()).backend().getOutgoing();
    if (outgoing != null) {
        outgoing.call(phoneNum);
    }
}
```
![outgoing](https://github.com/shweta-7span/plivo_masked_call/assets/109585734/79dbce3c-56d7-4370-8879-e3478d802a5d)
