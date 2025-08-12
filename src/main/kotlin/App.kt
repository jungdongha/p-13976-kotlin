package com.back

import java.io.File

class App {

    var lastId = 1
    val wiseSayings = mutableListOf<WiseSaying>()
    val dbDir = "db/wiseSaying"


    init{
        File(dbDir).mkdirs()
        val lastIdFile = File("$dbDir/lastId.txt")
        lastId = if (lastIdFile.exists()) {
            lastIdFile.readText().toIntOrNull() ?: 0
        } else {
            0
        }
        val dbDirFile = File(dbDir)
        val jsonFiles = dbDirFile.listFiles { _, name -> name.endsWith(".json") } ?: emptyArray()

        for (file in jsonFiles) {
            val jsonStr = file.readText()

            val idMatch = """"id": (\d+)""".toRegex().find(jsonStr)
            val contentMatch = """"content": "([^"]*)"""".toRegex().find(jsonStr)
            val authorMatch = """"author": "([^"]*)"""".toRegex().find(jsonStr)

            val id = idMatch?.groupValues?.get(1)?.toInt() ?: continue
            val content = contentMatch?.groupValues?.get(1) ?: ""
            val author = authorMatch?.groupValues?.get(1) ?: ""

            wiseSayings.add(WiseSaying(id, content, author))
        }
    }
    fun run() {
        println("==명언 앱==")




        while (true) {
            print("명령) ")
            val command = readln().trim()
            val rq = Rq(command)

            when(rq.action){
                "종료" ->{
                    break
                }

                "등록" ->{
                    val id = lastId++
                    println("명언 : ")
                    val content = readln().trim()
                    println("작가 : ")
                    val author = readln().trim()

                    val wiseSaying = WiseSaying(lastId, content, author)
                    wiseSayings.add(WiseSaying(id, content, author))

                    File("$dbDir/$lastId.json").writeText(wiseSaying.toJson())
                    File("$dbDir/lastId.txt").writeText(lastId.toString())
                    println("$id 번 명언이 등록되었습니다.")
                }
                "목록"->{
                    println("번호 / 작가 / 명언")
                    println("------------------")
                    if(wiseSayings.isEmpty()){
                        println("등록된 명언이 없습니다.")
                    }else{
                        for (wiseSaying in wiseSayings.sortedByDescending { it.id }) {
                            println("${wiseSaying.id} / ${wiseSaying.author} / ${wiseSaying.content}")
                        }
                    }
                }

                "삭제" ->{
                    val id = rq.getParamValueAsInt("id", 0)
                    if (id == 0) {
                        println("id를 정확히 입력해주세요")
                        continue
                    }

                    val wiseSaying = wiseSayings.firstOrNull { it.id == id }

                    if (wiseSaying != null) {
                        wiseSayings.remove(wiseSaying)
                        File("$dbDir/$id.json").delete()
                        println("${id}번 명언이 삭제되었습니다.")
                    } else {
                        println("${id}번 명언은 존재하지 않습니다.")
                    }
                }
                "수정"->{
                    val id = rq.getParamValueAsInt("id", 0)
                    if (id == 0) {
                        println("id를 정확히 입력해주세요")
                        continue
                    }

                    val wiseSaying = wiseSayings.firstOrNull(){ it.id == id}


                    if (wiseSaying != null) {
                        println("명언(기존) : ${wiseSaying.content}")
                        print("명언 : ")
                        val newContent = readln().trim()

                        println("작가(기존) : ${wiseSaying.author}")
                        print("작가 : ")
                        val newAuthor = readln().trim()

                        wiseSaying.content = newContent
                        wiseSaying.author = newAuthor

                        File("$dbDir/$id.json").writeText(wiseSaying.toJson())

                        println("${id}번 명언이 수정되었습니다.")
                    } else {
                        println("${id}번 명언은 존재하지 않습니다.")
                    }


                }

            }


        }
    }


}