//import grails.test.mixin.TestMixin
//import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
//@TestMixin(GrailsUnitTestMixin)
class StaticMethodMockBugSpec extends Specification {

    void "test that the stubbed static methods each return the right values"() {
        given: "test values"
        def firstValue = "first value test 1"
        def secondValue = "second value test 1"

        and: "a static mock on each class"
        GroovyMock(FirstSimpleClass, global: true)
        GroovyMock(SecondSimpleClass, global: true)

        and: "stubs of the two identically named methods"
        FirstSimpleClass.theStaticMethod() >> firstValue
        SecondSimpleClass.theStaticMethod() >> secondValue

        when: "we call the two stubbed static methods"
        def firstResult = FirstSimpleClass.theStaticMethod()
        def secondResult = SecondSimpleClass.theStaticMethod()

        then: "the mock that was declared first returns the right value"
        firstResult == firstValue

        and: "the mock that was declared second returns the right value"
        // the following should pass:
        //secondResult == secondValue
        // instead the following do:
        secondResult != secondValue
        secondResult == firstValue
    }

    void "test again, with reversed stub definition order"() {
        given: "test values"
        def firstValue = "first value test 2"
        def secondValue = "second value test 2"

        and: "a static mock on each class"
        GroovyMock(FirstSimpleClass, global: true)
        GroovyMock(SecondSimpleClass, global: true)

        and: "mocks of the two identically named methods, declared in the opposite order from before"
        SecondSimpleClass.theStaticMethod() >> secondValue
        FirstSimpleClass.theStaticMethod() >> firstValue

        when: "we call the two stubbed static methods"
        def firstResult = FirstSimpleClass.theStaticMethod()
        def secondResult = SecondSimpleClass.theStaticMethod()

        then: "the stub that was declared first returns the right value"
        // the following fails:
        //firstResult == firstValue
        // instead we see:
        firstResult == secondValue

        and: "the stub that was declared second returns the right value"
        secondResult == secondValue
    }

    void "test again, with reversed calling order"() {
        given: "test values"
        def firstValue = "first value test 1"
        def secondValue = "second value test 1"

        and: "a static mock on each class"
        GroovyMock(FirstSimpleClass, global: true)
        GroovyMock(SecondSimpleClass, global: true)

        and: "stubs of the two identically named methods"
        FirstSimpleClass.theStaticMethod() >> firstValue
        SecondSimpleClass.theStaticMethod() >> secondValue

        when: "we call the two stubbed static methods, in the opposite order from before"
        def secondResult = SecondSimpleClass.theStaticMethod()
        def firstResult = FirstSimpleClass.theStaticMethod()

        then: "the mock that was declared first returns the right value"
        firstResult == firstValue

        and: "the mock that was declared second returns the right value"
        // the following should pass:
        //secondResult == secondValue
        // instead the following do:
        secondResult != secondValue
        secondResult == firstValue
    }

    void "test control: first class only"() {
        given: "test value"
        def firstValue = "test value 4"

        and: "a static mock on the first class"
        GroovyMock(FirstSimpleClass, global: true)

        and: "mock of the static method"
        FirstSimpleClass.theStaticMethod() >> firstValue

        expect: "the mock to work properly"
        FirstSimpleClass.theStaticMethod() == firstValue
    }

    void "test control: second class only"() {
        given: "test values"
        def secondValue = "test value 5"

        and: "a static mock on the second class"
        GroovyMock(SecondSimpleClass, global: true)

        and: "mock of the static method"
        SecondSimpleClass.theStaticMethod() >> secondValue

        expect: "the mock to work properly"
        SecondSimpleClass.theStaticMethod() == secondValue
    }
}