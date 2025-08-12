package com.back

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import kotlin.test.AfterTest

class AppTest {
    private val dbDir = "db/wiseSaying"

    // 각 테스트가 실행되기 전에 항상 호출되는 함수X
    @BeforeEach
    fun beforeEach() {
        // 테스트를 위해 기존 db 폴더를 깨끗하게 삭제
        File(dbDir).deleteRecursively()
    }

    // 각 테스트가 끝난 후에 항상 호출되는 함수
    @AfterTest
    fun afterEach() {
        File(dbDir).deleteRecursively()
    }

    @Test
    fun `프로그램을 종료할 수 있다`() {
        val output = run("종료")
        assertTrue(output.contains("==명언 앱=="))
        assertTrue(output.contains("명령)"))
    }

    @Test
    fun `명언을 등록하고 목록을 볼 수 있다`() {
        val command = """
            등록
            현재를 사랑하라.
            작자미상
            등록
            과거에 집착하지 마라.
            홍길동
            목록
            종료
        """.trimIndent()

        val output = run(command)

        assertTrue(output.contains("1번 명언이 등록되었습니다."))
        assertTrue(output.contains("2번 명언이 등록되었습니다."))
        assertTrue(output.contains("2 / 홍길동 / 과거에 집착하지 마라."))
        assertTrue(output.contains("1 / 작자미상 / 현재를 사랑하라."))
    }

    @Test
    fun `명언을 삭제할 수 있다`() {
        val command = """
            등록
            삭제할 명언.
            삭제될 작가
            삭제?id=1
            목록
            종료
        """.trimIndent()

        val output = run(command)

        assertTrue(output.contains("1번 명언이 등록되었습니다."))
        assertTrue(output.contains("1번 명언이 삭제되었습니다."))
        assertTrue(output.contains("등록된 명언이 없습니다."))
    }


    // 테스트를 위한 헬퍼 함수
    private fun run(input: String): String {
        // 1. 표준 입력을 가짜 입력으로 교체
        val inputStream = ByteArrayInputStream(input.toByteArray())
        System.setIn(inputStream)

        // 2. 표준 출력을 우리가 가로챌 수 있는 통로로 교체
        val outputStream = ByteArrayOutputStream()
        val printStream = PrintStream(outputStream)
        val originalOut = System.out
        System.setOut(printStream)

        // 3. App 실행
        try {
            App().run()
        } finally {
            // 4. 테스트가 끝나면 원래의 표준 출력으로 복구
            System.setOut(originalOut)
        }

        // 5. 가로챈 출력 내용을 문자열로 반환
        return outputStream.toString()
    }
}
