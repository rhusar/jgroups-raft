package org.jgroups.raft.util.pmem;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

/**
 * Creates {@link FileChannel}.
 * <p>
 * If a Persistence Memory drive is available, support is provided by https://github.com/jhalliday/mashona
 *
 * @author Pedro Ruivo
 * @since 0.5.4
 */
public class FileProvider {

   private static final boolean ATTEMPT_PMEM;

   static {
      boolean attemptPmem = false;
      try {
         Class.forName("io.mashona.logwriting.PmemUtil");
         // use persistent memory if available, otherwise fallback to regular file.
         attemptPmem = true;
      } catch (ClassNotFoundException e) {
         //no op
      }
      ATTEMPT_PMEM = attemptPmem;
   }

   public static FileChannel openChannel(File file, int length, boolean create, boolean readSharedMetadata) throws IOException {
      FileChannel fileChannel = ATTEMPT_PMEM ? PmemUtilWrapper.pmemChannelFor(file, length, create, readSharedMetadata) : null;

      if (fileChannel == null) {
         if (!file.exists() && !create) {
            return null;
         }
         return FileChannel.open(file.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.READ);
      }

      return fileChannel;
   }

}
