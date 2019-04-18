package tn.disguisedtoast.drawable.settingsModule.models;

import java.util.ArrayList;
import java.util.List;

public final class LogInConfigs {

    public static List<String> getFacebookFunctionBody() {
        List<String> body = new ArrayList<>();
        body.add("const loading = await this.loadingController.create({");
        body.add("message: 'Please wait...'");
        body.add("});");
        body.add("this.presentLoading(loading);");
        body.add("let permissions = new Array<string>();");
        body.add("//the permissions your facebook app needs from the user");
        body.add("const permissions = [\"public_profile\", \"email\"];");
        body.add("this.fb.login(permissions).then(response =>{");
        body.add("let userId = response.authResponse.userID;");
        body.add("//Getting name and gender properties");
        body.add("this.fb.api(\"/me?fields=name,email\", permissions).then(user =>{");
        body.add("user.picture = \"https://graph.facebook.com/\" + userId + \"/picture?type=large\";");
        body.add("//now we have the users info, let's save it in the NativeStorage");
        body.add("this.nativeStorage.setItem('facebook_user', {");
        body.add("name: user.name,");
        body.add("email: user.email,");
        body.add("picture: user.picture");
        body.add("}).then(() =>{");
        body.add("this.router.navigate([\"/user\"]);");
        body.add("loading.dismiss();");
        body.add("}, error =>{");
        body.add("console.log(error);");
        body.add("loading.dismiss();");
        body.add("})");
        body.add("})");
        body.add("}, error =>{");
        body.add("console.log(error);");
        body.add("loading.dismiss();");
        body.add("});");

        return body;
    }

}
