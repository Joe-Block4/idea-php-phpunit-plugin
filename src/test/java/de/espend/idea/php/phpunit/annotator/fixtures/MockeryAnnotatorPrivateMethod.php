<?php

declare(strict_types=1);

namespace MockeryPlugin\DemoProject;

use Mockery;
use Mockery\Adapter\Phpunit\MockeryTestCase;
use Mockery\MockInterface;

class MockeryAnnotatorPrivateMethod extends MockeryTestCase
{

    /** @var Dependency|MockInterface */
    private $dependency;

    protected function setUp(): void
    {
        parent::setUp();
        $this->dependency = Mockery::mock(Dependency::class);
    }

    public function testWithExpects(): void
    {
        $this->dependency->expects('calledMethod')->andReturns('mocked result');
        $this->dependency->expects('<warning descr="Method 'privateMethod' is private, Mockery does not support private methods">privateMethod</warning>')->andReturns('mocked result');
    }

    public function testWithAllows(): void
    {
        $this->dependency->allows('calledMethod');
        $this->dependency->allows('<warning descr="Method 'privateMethod' is private, Mockery does not support private methods">privateMethod</warning>');
    }
}
