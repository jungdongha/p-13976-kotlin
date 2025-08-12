package com.back.controller

import com.back.Rq
import com.back.service.WiseSayingService
import kotlin.math.ceil

class WiseSayingController {
    private val wiseSayingService = WiseSayingService()

    fun write(rq: Rq) {
        print("명언 : ")
        val content = readln().trim()
        print("작가 : ")
        val author = readln().trim()
        val wiseSaying = wiseSayingService.write(content, author)
        println("${wiseSaying.id}번 명언이 등록되었습니다.")
    }

    fun list(rq: Rq) {
        val keywordType = rq.getParamValue("keywordType", "")
        val keyword = rq.getParamValue("keyword", "")
        val page = rq.getParamValueAsInt("page", 1)
        val pageSize = 5
        val filteredList = wiseSayingService.list(keywordType, keyword)

        if (keyword.isNotBlank()) {
            println("----------------------")
            println("검색타입 : $keywordType")
            println("검색어 : $keyword")
            println("----------------------")
        }

        println("번호 / 작가 / 명언")
        println("------------------")

        if (filteredList.isEmpty()) {
            println("등록된 명언이 없습니다.")
            return
        }

        val totalItems = filteredList.size
        val totalPages = ceil(totalItems.toDouble() / pageSize).toInt()
        val startIndex = (page - 1) * pageSize
        val endIndex = (startIndex + pageSize - 1).coerceAtMost(totalItems - 1)

        if (startIndex < totalItems) {
            for (i in startIndex..endIndex) {
                val wiseSaying = filteredList[i]
                println("${wiseSaying.id} / ${wiseSaying.author} / ${wiseSaying.content}")
            }
        }

        println("----------------------")
        print("페이지 : ")
        for (i in 1..totalPages) {
            if (i == page) {
                print("[$i] ")
            } else {
                print("$i ")
            }
        }
        println("/ $totalPages")
    }

    fun remove(rq: Rq) {
        val id = rq.getParamValueAsInt("id", 0)
        if (id == 0) {
            println("id를 정확히 입력해주세요")
            return
        }

        if (wiseSayingService.remove(id)) {
            println("${id}번 명언이 삭제되었습니다.")
        } else {
            println("${id}번 명언은 존재하지 않습니다.")
        }
    }

    fun modify(rq: Rq) {
        val id = rq.getParamValueAsInt("id", 0)
        if (id == 0) {
            println("id를 정확히 입력해주세요")
            return
        }

        val wiseSaying = wiseSayingService.findById(id)
        if (wiseSaying == null) {
            println("${id}번 명언은 존재하지 않습니다.")
            return
        }

        println("명언(기존) : ${wiseSaying.content}")
        print("명언 : ")
        val newContent = readln().trim()
        println("작가(기존) : ${wiseSaying.author}")
        print("작가 : ")
        val newAuthor = readln().trim()

        if (wiseSayingService.modify(id, newContent, newAuthor)) {
            println("${id}번 명언이 수정되었습니다.")
        }
    }

    fun build(rq: Rq) {
        wiseSayingService.build()
        println("data.json 파일의 내용이 갱신되었습니다.")
    }
}
