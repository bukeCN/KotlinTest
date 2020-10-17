package ffmpeg

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.*

fun main() {
    val path = "src/test/"
    var file = File(path)
    System.out.println(file.exists())
    System.out.println(file.absolutePath)
    var fileList = file.list()

    var count = 1;
    Arrays.sort(fileList,object:Comparator<String>{
        override fun compare(o1: String, o2: String): Int {
            System.out.println(o1)
            val end_1 = o1.indexOf('.')
            val end_2 = o2.indexOf('.')
            val a = o1.substring(0,end_1).toInt()
            val b = o2.substring(0,end_2).toInt()
            if (a > b){
                return 1
            } else if (a == b){
                return 0
            } else {
                return -1
            }
        }
    })

    fileList.forEach {
        System.out.println(it)
//        val newName = ffmpeg.rename(path, it, count)
        System.out.println("总计 ${fileList.size} : 当前第 ${count}")
        val shell = "ffmpeg -i ${file.absolutePath}/${it} -af atempo=0.8 ${file.absolutePath}/Slow_${it}"
        System.out.println("command> "+shell)

        executeCommand(shell)
//        ffmpeg.deleteSrc(file, newName)

        count++
    }

}

private fun deleteSrc(file: File, newName: String) {
    val srcFile = File("${file.absolutePath}/${newName}.wma")
    srcFile.delete()
}


private fun executeCommand(shell: String) {
    val p = shell.execute(shell)
    val exitCode = p.waitFor()
    val text = p.text()
    println(exitCode)
    println(text)
}

private fun rename(path: String, it: String, count: Int):String {
    var childFile = File(path + it)
    var noSpace = it.replace(" ","_")
    var childFileTo = File(path + noSpace)
    childFile.renameTo(childFileTo)
    return noSpace.removeSuffix(".wma")
}

private fun Process.text(): String {
    var output = ""
    //    输出 Shell 执行的结果
    val inputStream = this.inputStream
    val isr = InputStreamReader(inputStream)
    val reader = BufferedReader(isr)
    var line: String? = ""
    while (line != null) {
        line = reader.readLine()
        output += line + "\n"
    }
    return output
}

private fun String.execute(shell: String): Process {
    val runtime = Runtime.getRuntime()
    return runtime.exec(this)
}
