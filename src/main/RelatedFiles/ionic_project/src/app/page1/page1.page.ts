import {Component, OnInit} from '@angular/core';

@Component({
    selector: 'app-page1',
    templateUrl: './page1.page.html',
    styleUrls: ['./page1.page.scss'],
})
export class Page1Page implements OnInit {

    constructor() {
    }

    ngOnInit() {
    }


    async fbLogin() {
        const loading = await this.loadingController.create({
            message: 'Please wait...'
        });
        this.presentLoading(loading);
        let permissions = new Array<string>();
        //the permissions your facebook app needs from the user
        const permissions = ["public_profile", "email"];
        this.fb.login(permissions).then(response => {
            let userId = response.authResponse.userID;
            //Getting name and gender properties
            this.fb.api("/me?fields=name,email", permissions).then(user => {
                user.picture = "https://graph.facebook.com/" + userId + "/picture?type=large";
                //now we have the users info, let's save it in the NativeStorage
                this.nativeStorage.setItem('facebook_user', {
                    name: user.name,
                    email: user.email,
                    picture: user.picture
                }).then(() => {
                    this.router.navigate(["/user"]);
                    loading.dismiss();
                }, error => {
                    console.log(error);
                    loading.dismiss();
                })
            })
        }, error => {
            console.log(error);
            loading.dismiss();
        });
    }

}
