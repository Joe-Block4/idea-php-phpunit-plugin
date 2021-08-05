package de.espend.idea.php.phpunit.type;

import com.jetbrains.php.lang.PhpFileType;
import de.espend.idea.php.phpunit.PhpUnitLightCodeInsightFixtureTestCase;

/**
 * @author Daniel Espendiller <daniel@espendiller.net>
 * @see ProphecyArgumentTypeProvider
 */
public class ProphecyArgumentTypeProviderTest extends PhpUnitLightCodeInsightFixtureTestCase {
    public void setUp() throws Exception {
        super.setUp();
        myFixture.copyFileToProject("ProphecyArgumentTypeProvider.php");
    }

    public String getTestDataPath() {
        return "src/test/java/de/espend/idea/php/phpunit/type/fixtures";
    }

    public void testThatProphecyArgumentsProvideTypesForPrimitives() {
        assertMethodContainsTypes(PhpFileType.INSTANCE, "<?php\n" +
                "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
                "    {\n" +
                "        public function testFoobar()\n" +
                "        {\n" +
                "            $foo = $this->prophesize(Foo::class);\n" +
                "            $foo->getBar(\\Prophecy\\Argument::a<caret>ny());\n" +
                "        }\n" +
                "    }",
            "\\array"
        );

        assertMethodContainsTypes(PhpFileType.INSTANCE, "<?php\n" +
                "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
                "    {\n" +
                "        public function setUp()\n" +
                "        {\n" +
                "            $this->foo = $this->prophesize(Foo::class);\n" +
                "        }\n" +
                "        public function testFoobar()\n" +
                "        {\n" +
                "            $this->foo->getBar(\\Prophecy\\Argument::a<caret>ny());\n" +
                "        }\n" +
                "    }",
            "\\array"
        );
    }

    public void testThatProphecyArgumentsProvideTypesForClasses() {
        assertMethodContainsTypes(PhpFileType.INSTANCE, "<?php\n" +
                "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
                "    {\n" +
                "        public function testFoobar()\n" +
                "        {\n" +
                "            $foo = $this->prophesize(Foo::class);\n" +
                "            $foo->getBar(\\Prophecy\\Argument::any(), \\Prophecy\\Argument::a<caret>ny());\n" +
                "        }\n" +
                "    }",
            "\\Foo"
        );
    }
}
