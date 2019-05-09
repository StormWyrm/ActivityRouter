package com.github.stormwyrm.router.compiler.processor;

import com.github.stormwyrm.router.compiler.util.Consts;
import com.github.stormwyrm.router.compiler.util.Logger;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Map;

public abstract class BaseProcessor extends AbstractProcessor {

    protected Elements elementUtils;
    protected Filer filer;
    protected Types typeUtils;
    protected Logger logger;
    private String moduleName; //module名字

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        typeUtils = processingEnv.getTypeUtils();
        logger = new Logger(processingEnv.getMessager());

        logger.info(this.getClass().getSimpleName() + " init .");

        //获取用户配置参数
        Map<String, String> options = processingEnv.getOptions();
        if (options != null && options.size() != 0) {
            moduleName = options.get(Consts.KEY_MODULE_NAME);
        }

        if (moduleName != null && !moduleName.isEmpty()) {
            moduleName = moduleName.replaceAll("[^0-9a-zA-Z_]+", "");
            logger.info("The user has configuartion thr module name, it was [" + moduleName + "]");
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
