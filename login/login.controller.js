(function () {
    'use strict';

    angular
        .module('app')
        .controller('LoginController', LoginController);

    LoginController.$inject = ['$location', 'AuthenticationService', 'FlashService'];
    function LoginController($location, AuthenticationService, FlashService) {
        var vm = this;

        vm.login = login;
        AuthenticationService.ClearCredentials();

        function login() {
            vm.dataLoading = true;
            AuthenticationService.Login(vm.username, vm.password, function (response) {
                if (response.success) {
                    AuthenticationService.SetCredentials(vm.username, vm.password);
                    if(response.admin){
                      $location.path('/adminView');  
                    }else{
                    $location.path('/');
                    }
                }else {
                    FlashService.Error(response.message);
                    vm.dataLoading = false;
               }
            });
        };
    }
})();
