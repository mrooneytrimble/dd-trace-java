package com.datadog.profiling.mlt;

import java.io.IOException;
import java.lang.management.ThreadInfo;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JFRStackTraceSink implements StackTraceSink {
  private final Path baseDir;
  private final SamplerWriter writer = new SamplerWriter();

  public JFRStackTraceSink() throws IOException {
    this((Path) null);
  }

  public JFRStackTraceSink(String baseDir) throws IOException {
    this(baseDir != null ? Paths.get(baseDir) : null);
  }

  public JFRStackTraceSink(Path baseDir) throws IOException {
    this.baseDir = baseDir != null ? baseDir : Files.createTempDirectory("dd_mlt-");
  }

  @Override
  public void write(String[] ids, ThreadInfo[] threadInfos) {
    // TODO handle ids
    for (ThreadInfo threadInfo : threadInfos) {
      writer.writeThreadSample(threadInfo);
    }
  }

  @Override
  public int flush() {
    Path targetFile = baseDir.resolve(Long.toHexString(System.currentTimeMillis()) + ".jfr");
    try {
      int result = writer.dump(targetFile);
      log.debug("MLT recording was written to '{}' with length of {} bytes", targetFile, result);
      return result;
    } catch (IOException e) {
      log.error("Unable to write MLT recording: {}", targetFile, e);
    }
    return Integer.MIN_VALUE;
  }
}
