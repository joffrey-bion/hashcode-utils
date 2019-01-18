package org.hildan.hashcode.utils.solver

import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.function.Consumer

private const val INPUT_EXTENSION = ".in"
private const val OUTPUT_EXTENSION = ".out"

private const val INPUT_FOLDER = "inputs/"
private const val OUTPUT_FOLDER = "outputs/"

fun computeOutputFilename(inputFilename: String): String {
    val outputFilename = inputFilename
        .replace("^$INPUT_FOLDER".toRegex(), OUTPUT_FOLDER)
        .replace("/$INPUT_FOLDER".toRegex(), "/$OUTPUT_FOLDER")

    return if (outputFilename.endsWith(INPUT_EXTENSION)) {
        outputFilename.replace("$INPUT_EXTENSION$".toRegex(), OUTPUT_EXTENSION)
    } else {
        "$outputFilename.out"
    }
}

abstract class AbstractFileSolver : Consumer<String> {

    override fun accept(inputFilename: String) {
        val outputLines = solve(inputFilename)
        val outputFile = computeOutputFilename(inputFilename)
        try {
            writeOutputFile(outputFile, outputLines)
        } catch (e: IOException) {
            throw SolverException("Exception occurred while writing to the output file '$outputFile'", e)
        }
    }

    abstract fun solve(inputFilename: String): Iterable<CharSequence>

    private fun writeOutputFile(outputFilename: String, lines: Iterable<CharSequence>) {
        val filePath = Paths.get(outputFilename)
        val parentDir = filePath.parent
        if (parentDir != null) {
            Files.createDirectories(parentDir)
        }
        Files.write(
            filePath,
            lines,
            StandardCharsets.UTF_8,
            StandardOpenOption.CREATE,
            StandardOpenOption.WRITE,
            StandardOpenOption.TRUNCATE_EXISTING
        )
    }
}
