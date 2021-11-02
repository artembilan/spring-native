package com.example.integration;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.springframework.aot.context.bootstrap.generator.ApplicationContextAotProcessor;
import org.springframework.aot.context.bootstrap.generator.infrastructure.DefaultBootstrapWriterContext;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import com.squareup.javapoet.JavaFile;

public class CodeGenerator {

	public static void main(String[] args) {
		GenericApplicationContext context = prepareContext();
		ApplicationContextAotProcessor processor = new ApplicationContextAotProcessor(context.getClassLoader());
		DefaultBootstrapWriterContext writerContext = new DefaultBootstrapWriterContext(
				"org.springframework.aot", "ContextBootstrapInitializer");
		processor.process(context, writerContext);
		// In IntelliJ IDEA, make sure that "working directory" is set to $MODULE_DIR$
		Path srcDirectory = FileSystems.getDefault().getPath(".").resolve("src/main/java");
		for (JavaFile javaFile : writerContext.toJavaFiles()) {
			System.out.println(javaFile.toString());
		}
	}

	private static GenericApplicationContext prepareContext() {
		GenericApplicationContext context = new AnnotationConfigReactiveWebServerApplicationContext();
		context.registerBean(IntegrationApplication.class);
		return context;
	}

}
