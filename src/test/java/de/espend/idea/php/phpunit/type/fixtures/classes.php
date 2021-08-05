<?php

namespace PHPUnit\Framework
{
    class TestCase
    {
        static function createMock() {}
        static function prophesize() {}
    };
}

namespace
{
    class Foo
    {
        public function bar() {}
    }
}

namespace Prophecy\PhpUnit
{
    trait ProphecyTrait
    {
        protected function prophesize(?string $classOrInterface = null): ObjectProphecy
        {
        }
    }
}
