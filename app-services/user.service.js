(function () {
    'use strict';

    angular
        .module('app')
        .factory('UserService', UserService);

    UserService.$inject = ['$http','FlashService'];
    function UserService($http,FlashService) {
        var service = {};

        service.GetAll = GetAll;
        service.GetByUsername = GetByUsername;
        service.Create = Create;
        service.SendRequest = SendRequest;
        service.Accept = Accept;
        service.Reject = Reject;
        service.GetStatus = GetStatus;
        service.UpdateUser = UpdateUser;
        service.GetRelatedUsers = GetRelatedUsers;
        service.SendEmailRequest = SendEmailRequest;
        service.GetPassReqUsers = GetPassReqUsers;
        service.GetRequestStatus = GetRequestStatus;

        return service;
        
        function GetRequestStatus(userName) {
            return $http({
                    url: 'ServletCall',
                    method: "GET",
                    params: {action:'isRequested',username:userName},
                    headers: {'Content-Type': 'application/json'}
              }).success(handleSuccess)
              .error(handleError('Error Sending Email'));
        }
        
        function SendEmailRequest(emailId,insert) {
            return $http({
                    url: 'ServletCall',
                    method: "POST",
                    params: {action:'emailRequest',emailid:emailId,insert:insert},
                    headers: {'Content-Type': 'application/json'}
              }).success(handleSuccess)
              .error(handleError('Error Sending Email'));
        }
        
        function UpdateUser(user) {
            return $http({
                    url: 'ServletCall',
                    method: "POST",
                    params: {action:'updateUser',user:user},
                    headers: {'Content-Type': 'application/json'}
              }).success(handleSuccess)
              .error(handleError('Error Updating users'));
        }

        function GetAll(username) {
            return $http({
                    url: 'ServletCall',
                    method: "GET",
                    params: {action:'fetchAll',username:username},
                    headers: {'Content-Type': 'application/json'}
              }).success(handleSuccess)
              .error(handleError('Error Fetching All users'));
            //return $http.get('/api/users').then(handleSuccess, handleError('Error getting all users'));
        }
        
        function GetPassReqUsers() {
            return $http({
                    url: 'ServletCall',
                    method: "GET",
                    params: {action:'passRequstUsers'},
                    headers: {'Content-Type': 'application/json'}
              }).success(handleSuccess)
              .error(handleError('Error Fetching Password Reuwsted users'));
            //return $http.get('/api/users').then(handleSuccess, handleError('Error getting all users'));
        }
        
        function GetRelatedUsers(username) {
			return $http({
					url: 'ServletCall',
					method: "GET",
					params: {action:'relatedUsers',username:username},
					headers: {'Content-Type': 'application/json'}
				  }).success(handleSuccess)
				  .error(handleError('Error Fetching All users'));
            //return $http.get('/api/users').then(handleSuccess, handleError('Error getting all users'));
        }

        function GetByUsername(username) {
			return $http({
					url: 'ServletCall',
					method: "GET",
					params: {action:'fetchUser',username:username},
					headers: {'Content-Type': 'application/json'}
				  }).success(handleSuccess)
				  .error(handleError('Error Fetching user'));
            //return $http.get('/api/users/' + username).then(handleSuccess, handleError('Error getting user by username'));
        }

        function Create(user) {
		
			return $http({
					url: 'ServletCall',
					method: "POST",
					params: {action:'create',user:user},
					headers: {'Content-Type': 'application/json'}
				  }).success(handleSuccess)
				  .error(handleError('Error creating user'));
            //return $http.post('/api/users', user).then(handleSuccess, handleError('Error creating user'));
        }

        function SendRequest(username,profileUsername) {
			return $http({
					url: 'ServletCall',
					method: "POST",
					params: {action:'sendRequest',username:username,profileUsername:profileUsername},
					headers: {'Content-Type': 'application/json'}
				  }).success(handleSuccess)
				  .error(handleError('Error sending request'));
            //return $http.put('/api/users/' + user.id, user).then(handleSuccess, handleError('Error updating user'));
        }

        function Accept(username,profileUsername) {
            return $http({
					url: 'ServletCall',
					method: "POST",
					params: {action:'accept',username:username,profileUsername:profileUsername},
					headers: {'Content-Type': 'application/json'}
				  }).success(handleSuccess)
				  .error(handleError('Error Accepting Request'));
			//return $http.delete('/api/users/' + id).then(handleSuccess, handleError('Error deleting user'));
        }
		
        function Reject(username,profileUsername) {
            return $http({
					url: 'ServletCall',
					method: "POST",
					params: {action:'reject',username:username,profileUsername:profileUsername},
					headers: {'Content-Type': 'application/json'}
				  }).success(handleSuccess)
				  .error(handleError('Error Rejecting Request'));
			//return $http.delete('/api/users/' + id).then(handleSuccess, handleError('Error deleting user'));
        }
		
        function GetStatus(username,profileUsername) {
            return $http({
					url: 'ServletCall',
					method: "GET",
					params: {action:'getStatus',username:username,profileUsername:profileUsername},
					headers: {'Content-Type': 'application/json'}
				  }).success(handleSuccess)
				  .error(handleError('Error Rejecting Request'));
			//return $http.delete('/api/users/' + id).then(handleSuccess, handleError('Error deleting user'));
        }
	
        // private functions

        function handleSuccess(res) {
            return res.data;
        }

        function handleError(error) {
            //FlashService.Error('Failure');
            return function () {
                return { success: false, message: error };
            };
        }
    }

})();
