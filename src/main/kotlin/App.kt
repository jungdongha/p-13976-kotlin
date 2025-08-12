package com.back

class App {
    fun run() {
        println("==명언 앱==")
        var lastId = 1
        val wiseSayings = mutableListOf<WiseSaying>()

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

                    wiseSayings.add(WiseSaying(id, content, author))
                    println("$id 번 명언이 등록되었습니다.")
                }
                "목록"->{
                    println("번호 / 작가 / 명언")
                    println("------------------")
                    if(wiseSayings.isEmpty()){
                        println("등록된 명언이 없습니다.")
                    }else{
                        for(wiseSaying in wiseSayings){
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

                    val removed = wiseSayings.removeIf{it.id == id}

                    if (removed) {
                        println("${id}번 명언을 삭제하였습니다.")
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

                        println("${id}번 명언이 수정되었습니다.")
                    } else {
                        println("${id}번 명언은 존재하지 않습니다.")
                    }


                }

            }


        }
    }


}