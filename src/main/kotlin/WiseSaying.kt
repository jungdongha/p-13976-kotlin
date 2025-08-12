package com.back

data class WiseSaying (
    val id: Int,
    var content : String,
    var author : String
){

    fun toJson() : String{
        return """
            {
                "id": $id,
                "content": "$content",
                "author": "$author"
            }
            """.trimIndent()
    }

}
