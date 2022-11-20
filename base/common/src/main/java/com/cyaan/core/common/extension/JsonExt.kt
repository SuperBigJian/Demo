package com.cyaan.core.common.extension

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val COMMON_JSON = Json {
    prettyPrint = true //json格式化
    isLenient = true //宽松解析，json格式异常也可解析，如：{name:"小红",age:"18"} + Person(val name:String,val age:Int) ->Person("小红",18)
    ignoreUnknownKeys = true //忽略未知键，如{"name":"小红","age":"18"} ->Person(val name:String)
    coerceInputValues = true //强制输入值，如果json属性与对象格式不符，则使用对象默认值，如：{"name":"小红","age":null} + Person(val name:String = "小绿"，val age:Int = 18) ->Person("小红",18)
    encodeDefaults = true //编码默认值,默认情况下，默认值的属性不会参与序列化，通过设置encodeDefaults = true,可让默认属性参与序列化(可参考上述例子)
    allowStructuredMapKeys = true //允许结构化映射(map的key可以使用对象)
    allowSpecialFloatingPointValues = true //特殊浮点值：允许Double为NaN或无穷大
}

inline fun <reified T> String.toObj(
    json: () -> Json = {
        COMMON_JSON
    }
): T = json().decodeFromString(this)

fun Any.toJsonString(): String = COMMON_JSON.encodeToString(this)