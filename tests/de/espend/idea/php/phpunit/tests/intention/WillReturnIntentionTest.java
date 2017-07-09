package de.espend.idea.php.phpunit.tests.intention;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.PhpFileType;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import de.espend.idea.php.phpunit.intention.WillReturnIntention;
import de.espend.idea.php.phpunit.tests.PhpUnitLightCodeInsightFixtureTestCase;

import java.io.File;

/**
 * @author Daniel Espendiller <daniel@espendiller.net>
 * @see de.espend.idea.php.phpunit.intention.WillReturnIntention
 */
public class WillReturnIntentionTest extends PhpUnitLightCodeInsightFixtureTestCase {
    public void setUp() throws Exception {
        super.setUp();
        myFixture.copyFileToProject("classes.php");
    }

    public String getTestDataPath() {
        return new File(this.getClass().getResource("fixtures").getFile()).getAbsolutePath();
    }

    public void testThatIntentionForWillReturnProvidesIsAvailable() {
        assertIntentionIsAvailable(PhpFileType.INSTANCE, "<?php\n" +
                "/** @var $x \\PHPUnit\\Framework\\TestCase */\n" +
                "$x->createMock(\\Foo\\Bar::class);" +
                "$x->method('getFooBar')->willReturn(<caret>)",
            "PHPUnit: Add mock method"
        );
    }

    public void testThatInspectionIsInvokedForMockBuilderWithPropertyAccess() {
        myFixture.configureByText(PhpFileType.INSTANCE, "<?php\n" +
            "/** @var $x \\PHPUnit\\Framework\\TestCase */\n" +
            "$x->createMock(\\Foo\\Bar::class);" +
            "$x->method('getFooBar')->willReturn(<caret>)"
        );

        String text = invokeAndGetText();
        assertTrue(text.contains("$this->foo->method('getFooBar')->willReturn();"));

        PsiElement target = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        assertEquals("willReturn", ((MethodReference) target.getParent()).getName());
    }

    private String invokeAndGetText() {
        PsiElement psiElement = myFixture.getFile().findElementAt(myFixture.getCaretOffset());

        new WillReturnIntention().invoke(getProject(), getEditor(), psiElement);

        return psiElement.getContainingFile().getText();
    }
}
