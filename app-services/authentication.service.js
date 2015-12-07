(function () {
    'use strict';

    angular
        .module('app')
        .factory('AuthenticationService', AuthenticationService);

    AuthenticationService.$inject = ['$http', '$cookieStore', '$rootScope', '$timeout', 'UserService'];
    function AuthenticationService($http, $cookieStore, $rootScope, $timeout, UserService) {
        var service = {};

        service.Login = Login;
        service.SetCredentials = SetCredentials;
        service.ClearCredentials = ClearCredentials;

        return service;

        function Login(username, password, callback) {
		
            /* Dummy authentication for testing, uses $timeout to simulate api call
             ----------------------------------------------*/ 
                var response;
                UserService.GetByUsername(username)
                    .then(function (resp) {
                        if (resp.data.username !== null && resp.data.password === password) {
                            response = { success: true , admin: resp.data.admin};
                        } else {
                            response = { success: false, message: resp.data.password};
                        }
                        callback(response);
                    });

	    }

        function SetCredentials(username, password) {
            $rootScope.globals = {
                currentUser: {
                    username: username,
                    password: password
                }
            };
            $cookieStore.put('globals', $rootScope.globals);
        }

        function ClearCredentials() {
            $rootScope.globals = {};
            $cookieStore.remove('globals');
        }
    }

})();