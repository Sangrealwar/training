import groovy.json.JsonSlurper

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/10/15.
 */
//app = new GroovyApp()
//app.hello()




def jsonResult = "http://m.weather.com.cn/data/".toURL().getText()
def jsonParser = new JsonSlurper().parseText(jsonResult)
jsonParser?.weatherinfo?.with {
    println "temp: $temp1 @ city: $city"
}