package org.spockframework.compiler.model;

import java.util.Set;
public interface IBlockParseInfo {
  public Block addNewBlock(Method method);
  public Set<? extends IBlockParseInfo> getSuccessors(Method method);
}
