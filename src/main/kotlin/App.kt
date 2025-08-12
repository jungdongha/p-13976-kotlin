package com.back

import com.back.controller.WiseSayingController

class App {
    fun run() {
        println("==명언 앱==")
        val wiseSayingController = WiseSayingController()

        while (true) {
            print("명령) ")
            val command = readln().trim()
            val rq = Rq(command)

            when (rq.action) {
                "종료" -> break
                "등록" -> wiseSayingController.write(rq)
                "목록" -> wiseSayingController.list(rq)
                "삭제" -> wiseSayingController.remove(rq)
                "수정" -> wiseSayingController.modify(rq)
                "빌드" -> wiseSayingController.build(rq)
                else -> println("올바르지 않은 명령입니다.")
            }
        }
    }
}