/**
 * Created by wq on 2017/3/16.
 */

//自定义服务方法，是一个普通的function
angular.module('myApp').factory('SpitterService', ['$http', '$q', function($http, $q){

    // var REST_SERVICE_URI = 'http://localhost:8080/Spring4MVCAngularJSExample/user/';
    var REST_SERVICE_URI = 'http://localhost:82/spitter/list.do';
    var CREATE_URI = 'http://localhost:82/spitter/add.do';
    var UPDATE_URI = 'http://localhost:82/spitter/update.do';
    var DELETE_URI = 'http://localhost:82/spitter/delete.do';

    var factory = {
        fetchAllSpitters: fetchAllSpitters,
        createSpitter: createSpitter,
        updateSpitter:updateSpitter,
        deleteSpitter:deleteSpitter
    };

    return factory;

    function fetchAllSpitters() {
        var deferred = $q.defer();
        $http.get('/spitter/list.do')
            .then(
                function (response) {
                    deferred.resolve(response.data);   //返回的是响应流对象不是服务端传过来的json对象
                },
                function(errResponse){
                    alert('获取出错');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function createSpitter(spitter) {
        var deferred = $q.defer();
        $http
        ({
            url:CREATE_URI,
            method:'POST',
            data:spitter
        }).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    alert('失败了');
                    console.error('Error while creating User');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }


    function updateSpitter(spitter, id) {
        var deferred = $q.defer();
        $http({
            url:UPDATE_URI+"/"+id,
            method:'PUT',
            data:spitter})
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while updating User');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function deleteSpitter(id) {
        var deferred = $q.defer();
        $http({
            url:DELETE_URI+"/"+id,
            method:'DELETE'})
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while deleting User');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

}]);