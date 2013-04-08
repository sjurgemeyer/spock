package org.spockframework.compiler.model;

import java.util.Set;
import java.util.EnumSet;

public class BlockParseInfoManager {

    private static Set<? extends IBlockParseInfo> values = EnumSet.allOf(BlockParseInfo.class);

    public static Set<? extends IBlockParseInfo> values() {
      return values;
    }
}
