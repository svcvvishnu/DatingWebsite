(function () {
    'use strict';

    angular
        .module('app')
        .controller('ForgotPasswordController', ForgotPasswordController);

    ForgotPasswordController.$inject = [ 'UserService', 'FlashService'];
    function ForgotPasswordController( UserService,  FlashService) {
        var vm = this;

        vm.sendEmail = sendEmail;

        function sendEmail() {
            UserService.SendEmailRequest(vm.emailid,'true')
                .then(function (response) {
                    FlashService.Success('Requested sent to Admin successfully', false);
                });
            
        }
    }

})();
