/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2008-present Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.repository.security;

import org.sonatype.goodies.testsupport.TestSupport;
import org.sonatype.nexus.repository.Format;
import org.sonatype.nexus.repository.Repository;
import org.sonatype.nexus.repository.security.internal.SimpleVariableResolverAdapter;
import org.sonatype.nexus.repository.storage.Asset;
import org.sonatype.nexus.repository.view.Request;
import org.sonatype.nexus.selector.VariableSource;

import com.orientechnologies.orient.core.record.impl.ODocument;
import org.junit.Test;
import org.mockito.Mock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class SimpleVariableResolverAdapterTest
    extends TestSupport
{
  private static final String FORMAT_VARIABLE = "format";

  private static final String PATH_VARIABLE = "path";

  private static final String TEST_PATH = "/some/path.txt";

  private static final String TEST_FORMAT = "test";

  @Mock
  Request request;

  @Mock
  ODocument document;

  @Mock
  Asset asset;

  @Mock
  Repository repository;

  @Test
  public void testFromRequest() throws Exception {
    when(request.getPath()).thenReturn(TEST_PATH);
    when(repository.getName()).thenReturn("SimpleVariableResolverAdapterTest");
    when(repository.getFormat()).thenReturn(new Format(TEST_FORMAT) { });
    SimpleVariableResolverAdapter simpleVariableResolverAdapter = new SimpleVariableResolverAdapter();
    VariableSource source = simpleVariableResolverAdapter.fromRequest(request, repository);

    assertThat(source.getVariableSet(), containsInAnyOrder(FORMAT_VARIABLE, PATH_VARIABLE));
    assertThat(source.get(FORMAT_VARIABLE).get(), is(TEST_FORMAT));
    assertThat(source.get(PATH_VARIABLE).get(), is(TEST_PATH));
  }

  @Test
  public void testFromDocument() throws Exception {
    when(document.field("name", String.class)).thenReturn(TEST_PATH);
    when(document.field(FORMAT_VARIABLE, String.class)).thenReturn(TEST_FORMAT);

    SimpleVariableResolverAdapter simpleVariableResolverAdapter = new SimpleVariableResolverAdapter();
    VariableSource source = simpleVariableResolverAdapter.fromDocument(document);

    assertThat(source.getVariableSet(), containsInAnyOrder(FORMAT_VARIABLE, PATH_VARIABLE));
    assertThat(source.get(FORMAT_VARIABLE).get(), is(TEST_FORMAT));
    assertThat(source.get(PATH_VARIABLE).get(), is(TEST_PATH));
  }

  @Test
  public void testFromAsset() throws Exception {
    when(asset.name()).thenReturn(TEST_PATH);
    when(asset.format()).thenReturn(TEST_FORMAT);

    SimpleVariableResolverAdapter simpleVariableResolverAdapter = new SimpleVariableResolverAdapter();
    VariableSource source = simpleVariableResolverAdapter.fromAsset(asset);

    assertThat(source.getVariableSet(), containsInAnyOrder(FORMAT_VARIABLE, PATH_VARIABLE));
    assertThat(source.get(FORMAT_VARIABLE).get(), is(TEST_FORMAT));
    assertThat(source.get(PATH_VARIABLE).get(), is(TEST_PATH));
  }
}
