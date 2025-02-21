////////////////////////////////////////////////////////////////////////////////
// Copyright 2022 Prominic.NET, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License
//
// Author: Prominic.NET, Inc.
// No warranty of merchantability or fitness of any kind.
// Use this software at your own risk.
////////////////////////////////////////////////////////////////////////////////
package net.prominic.groovyls.providers;

import net.prominic.groovyls.compiler.ast.ASTContext;
import net.prominic.groovyls.compiler.util.GroovyASTUtils;
import net.prominic.groovyls.util.GroovyLSUtils;
import org.codehaus.groovy.ast.ASTNode;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.LocationLink;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.eclipse.lsp4j.jsonrpc.messages.Either;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TypeDefinitionProvider extends DocProvider {

    public TypeDefinitionProvider(URI doc, ASTContext astContext) {
        super(doc, astContext);
    }

    public CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> provideTypeDefinition(
                                                                                                                   TextDocumentIdentifier textDocument,
                                                                                                                   Position position) {
        ASTNode offsetNode = astContext.getVisitor().getNodeAtLineAndColumn(doc, position.getLine(), position.getCharacter());
        if (offsetNode == null) {
            return CompletableFuture.completedFuture(Either.forLeft(Collections.emptyList()));
        }

        ASTNode definitionNode = GroovyASTUtils.getTypeDefinition(offsetNode, astContext);
        if (definitionNode == null || definitionNode.getLineNumber() == -1 || definitionNode.getColumnNumber() == -1) {
            return CompletableFuture.completedFuture(Either.forLeft(Collections.emptyList()));
        }

        URI definitionURI = astContext.getVisitor().getURI(definitionNode);
        if (definitionURI == null) {
            definitionURI = doc;
        }

        Location location = GroovyLSUtils.astNodeToLocation(definitionNode, definitionURI);
        if (location == null) {
            return CompletableFuture.completedFuture(Either.forLeft(Collections.emptyList()));
        }

        return CompletableFuture.completedFuture(Either.forLeft(Collections.singletonList(location)));
    }
}
