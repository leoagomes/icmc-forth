import java.io.*

class Emitter(outFilePath: String){
    private var outputFile : FileOutputStream = FileOutputStream(outFilePath)

    fun rawEmit(data: String) {
        outputFile.write(data.toByteArray())
    }

    fun rawEmitLine(data: String) {
        rawEmit(data + "\r\n")
    }

    fun emitLabel(label: String) {
        rawEmitLine("$label:")
    }

    fun emitRawInstruction(instText: String) {
        rawEmitLine(instText)
    }

    fun close() {
        outputFile.close()
    }
}