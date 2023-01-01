package com.aisvalidator.base.domain.contracts;

import com.aisvalidator.base.domain.model.AISEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventDispatcherTests {
  static class TestEvent1 extends AISEvent { }

  static class TestConsumer1 implements EventConsumer<TestEvent1> {
    @Override public void accept(TestEvent1 event) {
    }
  }

  static class TestEvent2 extends AISEvent { }

  static class TestConsumer2 implements EventConsumer<TestEvent2> {
    @Override public void accept(TestEvent2 event) {
    }
  }

  @Test
  void sunshine() {
    //GIVEN
    EventDispatcher dispatcher = clazz -> {};
    TestConsumer1 c11 = new TestConsumer1();

    //WHEN
    dispatcher.register(TestEvent1.class, c11);

    //THEN
    assertEquals(1, dispatcher.consumers.size());
    assertEquals(TestEvent1.class.getName(), dispatcher.consumers.keySet().iterator().next().getName());

    //GIVEN
    TestConsumer1 c12 = new TestConsumer1();

    //WHEN
    dispatcher.register(TestEvent1.class, c12);

    //THEN
    assertEquals(1, dispatcher.consumers.size());
    assertEquals(2, dispatcher.consumers.entrySet().iterator().next().getValue().size());
    assertEquals(TestEvent1.class.getName(), dispatcher.consumers.keySet().iterator().next().getName());

    //GIVEN
    TestConsumer2 c21 = new TestConsumer2();

    //WHEN
    dispatcher.register(TestEvent2.class, c21);

    //THEN
    assertEquals(2, dispatcher.consumers.size());
  }
}
