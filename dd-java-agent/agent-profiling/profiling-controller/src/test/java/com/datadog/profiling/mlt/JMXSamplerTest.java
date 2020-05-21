package com.datadog.profiling.mlt;

import datadog.trace.core.util.ThreadStackAccess;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class JMXSamplerTest {

  @Test
  public void sampler() throws Exception {
    ThreadStackAccess.enableJmx();
    StackTraceSink sink = new JFRStackTraceSink();
    JMXSampler sampler = new JMXSampler(sink);
    sampler.addThreadId(Thread.currentThread().getId());
    sampler.addThreadId(1);
    Thread.sleep(100);
    sampler.removeThread(1);
    sampler.removeThread(Thread.currentThread().getId());
    sampler.shutdown();
    int bufferLength = sink.flush();
    Assert.assertTrue(bufferLength > 0);
  }

  @Test
  public void emptySampler() throws Exception {
    ThreadStackAccess.enableJmx();
    StackTraceSink sink = new JFRStackTraceSink();
    JMXSampler sampler = new JMXSampler(sink);
    Thread.sleep(100);
    sampler.shutdown();
    int bufferLength = sink.flush();
    Assert.assertTrue(bufferLength > 0);
  }
}
