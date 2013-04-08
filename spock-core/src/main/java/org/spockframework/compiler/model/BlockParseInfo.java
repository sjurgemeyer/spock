/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spockframework.compiler.model;

import java.util.EnumSet;
import java.util.Set;

/**
 *
 * @author Peter Niederwieser
 */
public enum BlockParseInfo implements IBlockParseInfo {


  AND {
    public Set<? extends IBlockParseInfo> getSuccessors(Method method) {
      return method.getLastBlock().getParseInfo().getSuccessors(method);
    }
    public Block addNewBlock(Method method) {
      return method.getLastBlock();
    }
  },

  ANONYMOUS {
    public Block addNewBlock(Method method) {
      return method.addBlock(new AnonymousBlock(method));
    }
    public Set<? extends IBlockParseInfo> getSuccessors(Method method) {
      if (successors == null) {
        successors = EnumSet.of(SETUP, GIVEN, EXPECT, WHEN, CLEANUP, WHERE, METHOD_END);
      }
      return successors;
    }
  },

  SETUP {
    public Block addNewBlock(Method method) {
      return method.addBlock(new SetupBlock(method));
    }
    public Set<? extends IBlockParseInfo> getSuccessors(Method method) {
      if (successors == null) {
        successors = EnumSet.of(AND, EXPECT, WHEN, CLEANUP, WHERE, METHOD_END);
      }
      return successors;
    }
  },

  GIVEN {
    public Block addNewBlock(Method method) {
      return SETUP.addNewBlock(method);
    }
    public Set<? extends IBlockParseInfo> getSuccessors(Method method) {
      if (successors == null) {
        successors = SETUP.getSuccessors(method);
      }
      return successors;
    }
  },

  EXPECT {
    public Block addNewBlock(Method method) {
      return method.addBlock(new ExpectBlock(method));
    }
    public Set<? extends IBlockParseInfo> getSuccessors(Method method) {
      if (successors == null) {
        successors = EnumSet.of(AND, WHEN, CLEANUP, WHERE, METHOD_END);
      }
      return successors;
    }
  },

  WHEN {
    public Block addNewBlock(Method method) {
      return method.addBlock(new WhenBlock(method));
    }
    public Set<? extends IBlockParseInfo> getSuccessors(Method method) {
        if (this.successors == null) {
          this.successors = EnumSet.of(AND, THEN);
        }
        return this.successors;
    }
  },

  THEN {
    public Block addNewBlock(Method method) {
      return method.addBlock(new ThenBlock(method));
    }
    public Set<? extends IBlockParseInfo> getSuccessors(Method method) {
        if (successors == null) {
            successors = EnumSet.of(AND, EXPECT, WHEN, THEN, CLEANUP, WHERE, METHOD_END);
        }
        return successors;
    }
  },

  CLEANUP {
    public Block addNewBlock(Method method) {
      return method.addBlock(new CleanupBlock(method));
    }
    public Set<? extends IBlockParseInfo> getSuccessors(Method method) {
        if (successors == null) {
            successors = EnumSet.of(AND, WHERE, METHOD_END);
        }
        return successors;
    }
  },

  WHERE {
    public Block addNewBlock(Method method) {
      return method.addBlock(new WhereBlock(method));
    }
    public Set<? extends IBlockParseInfo> getSuccessors(Method method) {
        if (successors == null) {
            successors = EnumSet.of(AND, METHOD_END);
        }
        return successors;
    }
  },

  METHOD_END {
    public Block addNewBlock(Method method) {
      throw new UnsupportedOperationException("addNewBlock");
    }
    public Set<? extends IBlockParseInfo> getSuccessors(Method method) {
      throw new UnsupportedOperationException("getSuccessors");
    }
    public String toString() {
      return "end-of-method";
    }
  };

  protected Set<? extends IBlockParseInfo> successors;
  public String toString() {
    return super.toString().toLowerCase();
  }

  public abstract Block addNewBlock(Method method);

  public abstract Set<? extends IBlockParseInfo> getSuccessors(Method method);
}
