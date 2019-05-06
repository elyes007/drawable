package tn.disguisedtoast.drawable.settingsModule.models;

import java.util.ArrayList;
import java.util.List;

public final class LogInConfigs {

    public static List<String> getFacebookFunctionBody() {
        List<String> body = new ArrayList<>();
        body.add("if(this.platfom.is('cordova')) {");
        body.add("this.fb.login(['email', 'public_profile']).then(res => {");
        body.add("const facebookCredential = auth.FacebookAuthProvider.credential(res.authResponse.accessToken)");
        body.add("auth().signInWithCredential(facebookCredential);");
        body.add("})");
        body.add("}else {");
        body.add("this.afAuth.auth.signInWithPopup(new auth.FacebookAuthProvider()).then(resp => {");
        body.add("console.log(resp)");
        body.add("}).catch(e => {");
        body.add("console.log(e)");
        body.add("})");
        body.add("}");
        return body;
    }

}
