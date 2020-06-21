package com.adacore.adaintellij;

import com.intellij.testFramework.fixtures.IdeaTestExecutionPolicy;
import com.intellij.testFramework.fixtures.TempDirTestFixture;
import com.intellij.testFramework.fixtures.impl.LightTempDirTestFixtureImpl;
import com.intellij.testFramework.fixtures.impl.TempDirTestFixtureImpl;

public class AdaTempDirTestFixture extends IdeaTestExecutionPolicy {
    @Override
    protected String getName() {
        return null;
    }

    public TempDirTestFixture createTempDirTestFixture() {
        return new TempDirTestFixtureImpl();
    }
}
