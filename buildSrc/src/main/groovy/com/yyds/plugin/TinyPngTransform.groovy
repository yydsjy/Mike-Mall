package com.yyds.plugin

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import javassist.ClassPool
import javassist.CtClass
import javassist.bytecode.ClassFile
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.gradle.api.Project

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream


class TinyPngTransform extends Transform {

    private ClassPool classPool = ClassPool.getDefault()

    TinyPngTransform(Project project) {
        classPool.appendClassPath(project.android.bootClasspath[0].toString())

        classPool.importPackage("android.os.Bundle")
        classPool.importPackage("android.widget.Toast")
        classPool.importPackage("android.app.Activity")
    }

    @Override
    String getName() {
        return "TinyPngTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        def outputProvider = transformInvocation.outputProvider
        transformInvocation.inputs.each { input ->
            input.directoryInputs.each { dirInput ->
                println("dirInput abs file path: " + dirInput.file.absolutePath)
                handleDirectory(dirInput.file)

                def dest = outputProvider.getContentLocation(dirInput.name, dirInput.contentTypes, dirInput.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(dirInput.file, dest)
            }

            input.jarInputs.each { jarInput ->
                println("dirInput abs file path: " + jarInput.file.absolutePath)
                def srcFile = handleJar(jarInput.file)

                def jarName = jarInput.name
                def md5 = DigestUtils.md5Hex(jarInput.file.absolutePath)
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }
                def dest = outputProvider.getContentLocation(md5 + jarName, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                FileUtils.copyFile(srcFile, dest)
            }
        }
        classPool.clearImportedPackages()
    }

    void handleDirectory(File dir) {
        classPool.appendClassPath(dir.absolutePath)

        if (dir.isDirectory()) {
            dir.eachFileRecurse { file ->
                def filePath = file.absolutePath
                println("handleDirectory file path: " + filePath)
                if (shouldModifyClass(filePath)) {
                    def inputStream = new FileInputStream(file)
                    def ctClass = modifyClass(inputStream)
                    ctClass.writeFile(dir.name)
                    ctClass.detach()
                }
            }
        }
    }

    File handleJar(File jarFile) {
        classPool.appendClassPath(jarFile.absolutePath)
        def inputJarFile = new JarFile(jarFile)
        def enumeration = inputJarFile.entries()

        def outputJarFile = new File(jarFile.parentFile, "temp_" + jarFile.name)
        if (outputJarFile.exists()) outputJarFile.delete()
        def jarOutputStream = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(outputJarFile)))
        while (enumeration.hasMoreElements()) {
            def inputJarEntry = enumeration.nextElement()
            def inputJarEntryName = inputJarEntry.name

            def outputJarEntry = new JarEntry(inputJarEntryName)
            jarOutputStream.putNextEntry(outputJarEntry)
            println("inputJarEntryName: " + inputJarEntryName)

            def inputStream = inputJarFile.getInputStream(inputJarEntry)
            if (!shouldModifyClass(inputJarEntryName)) {
                jarOutputStream.write(IOUtils.toByteArray(inputStream))
                inputStream.close()
                continue
            }

            def ctClass = modifyClass(inputStream)
            def byteCode = ctClass.toBytecode()
            ctClass.detach()
            inputStream.close()

            jarOutputStream.write(byteCode)
            jarOutputStream.flush()
        }

        inputJarFile.close()
        jarOutputStream.closeEntry()
        jarOutputStream.flush()
        jarOutputStream.close()
        return outputJarFile
    }

    CtClass modifyClass(InputStream is) {
        def classFile = new ClassFile(new DataInputStream(new BufferedInputStream(is)))
        println("modifyClass name: " + classFile.name)
        def ctClass = classPool.get(classFile.name)
        if (ctClass.isFrozen()) {
            ctClass.defrost()
        }

        def bundle = classPool.get("android.os.Bundle")
        CtClass[] params = Arrays.asList(bundle).toArray()
        def method = ctClass.getDeclaredMethod("onCreate", params)
        def message = classFile.name
        method.insertAfter("Toast.makeText(this," + "\"" + message + "\"" + ",Toast.LENGTH_SHORT).show();")
        return ctClass
    }

    boolean shouldModifyClass(String filePath) {
        return (filePath.contains("com/yyds")
                && filePath.endsWith("Activity.class")
                && !filePath.contains("R.class")
                && !filePath.contains('$')
                && !filePath.contains('R$')
                && !filePath.contains("BuildConfig.class")
                && !filePath.contains("Hilt"))
    }
}