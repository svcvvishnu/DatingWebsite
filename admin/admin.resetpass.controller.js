(function () {
    'use strict';

    angular
        .module('app')
        .controller('AdminProfileController', AdminProfileController);

    AdminProfileController.$inject = [ 'UserService','$routeParams', 'FlashService'];
    function AdminProfileController( UserService, $routeParams ,FlashService) {
        var vm = this;

        vm.profileUser = null;
        vm.resetPassword = resetPassword;
        vm.hasPasswordRequest = true;
        vm.profileUsername = $routeParams.profileUsername;
        
        vm.resetPassword = resetPassword;
                 
        getProfileUser();
        hasRequest();
        
        function getProfileUser() {
            UserService.GetByUsername(vm.profileUsername)
            .then(function (response) {
                vm.profileUser = response.data;
            });
        }
        
        function hasRequest() {
            UserService.GetRequestStatus(vm.profileUsername)
            .then(function (response) {
                vm.hasPasswordRequest = response.data.isRequested;
            });
        }
        
        function resetPassword() {
            UserService.SendEmailRequest(vm.profileUser.emailid,'false')
            .then(function (response) {
                FlashService.Success('Password successfully sent to ' + vm.profileUser.emailid ,true);
                vm.hasPasswordRequest = false;
            });
        }
    }
    
})();
