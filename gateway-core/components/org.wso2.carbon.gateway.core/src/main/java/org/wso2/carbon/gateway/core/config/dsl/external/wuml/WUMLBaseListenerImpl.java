/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 * <p>
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.carbon.gateway.core.config.dsl.external.wuml;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.gateway.core.Constants;
import org.wso2.carbon.gateway.core.config.ConfigConstants;
import org.wso2.carbon.gateway.core.config.Integration;
import org.wso2.carbon.gateway.core.config.IntegrationConfigRegistry;
import org.wso2.carbon.gateway.core.config.Parameter;
import org.wso2.carbon.gateway.core.config.ParameterHolder;
import org.wso2.carbon.gateway.core.config.dsl.external.StringParserUtil;
import org.wso2.carbon.gateway.core.config.dsl.external.WUMLConfigurationBuilder;
import org.wso2.carbon.gateway.core.config.dsl.external.wuml.generated.WUMLBaseListener;
import org.wso2.carbon.gateway.core.config.dsl.external.wuml.generated.WUMLParser;
import org.wso2.carbon.gateway.core.flow.Mediator;
import org.wso2.carbon.gateway.core.flow.MediatorProviderRegistry;
import org.wso2.carbon.gateway.core.flow.Resource;
import org.wso2.carbon.gateway.core.flow.templates.uri.URITemplate;
import org.wso2.carbon.gateway.core.flow.templates.uri.URITemplateException;
import org.wso2.carbon.gateway.core.inbound.InboundEPProviderRegistry;
import org.wso2.carbon.gateway.core.inbound.InboundEndpoint;
import org.wso2.carbon.gateway.core.outbound.OutboundEPProviderRegistry;
import org.wso2.carbon.gateway.core.outbound.OutboundEndpoint;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Implementation class of the ANTLR generated listener class
 */
public class WUMLBaseListenerImpl extends WUMLBaseListener {
    private static final Logger log = LoggerFactory.getLogger(WUMLBaseListenerImpl.class);
    private Integration integration;

    public WUMLBaseListenerImpl() {
    }

    public WUMLConfigurationBuilder.IntegrationFlow getIntegrationFlow() {
        return null;
    }

    @Override
    public void enterSourceFile(WUMLParser.SourceFileContext ctx) {
        integration = new Integration("name");
    }

    @Override
    public void exitSourceFile(WUMLParser.SourceFileContext ctx) {
        IntegrationConfigRegistry.getInstance().addIntegrationConfig(integration);
    }

    @Override
    public void enterDefinition(WUMLParser.DefinitionContext ctx) {
    }

    @Override
    public void exitDefinition(WUMLParser.DefinitionContext ctx) {
    }

    @Override
    public void enterConstants(WUMLParser.ConstantsContext ctx) {
    }

    @Override
    public void exitConstants(WUMLParser.ConstantsContext ctx) {
    }

    @Override
    public void enterResources(WUMLParser.ResourcesContext ctx) {
    }

    @Override
    public void exitResources(WUMLParser.ResourcesContext ctx) {
    }

    @Override
    public void enterPackageDef(WUMLParser.PackageDefContext ctx) {
    }

    @Override
    public void exitPackageDef(WUMLParser.PackageDefContext ctx) {
        //String packageName = ctx.qualifiedName().getText();
        /* Updating the integration name to its fully qualified name */
        //integration.setName(packageName + "." + integration.getName());
    }

    @Override
    public void enterPath(WUMLParser.PathContext ctx) {
    }

    @Override
    public void exitPath(WUMLParser.PathContext ctx) {
        integration.getAnnotation(ConfigConstants.AN_BASE_PATH)
                .setValue(StringParserUtil.getValueWithinDoubleQuotes(ctx.StringLiteral().getText()));
    }

    @Override
    public void enterSource(WUMLParser.SourceContext ctx) {
    }

    @Override
    public void exitSource(WUMLParser.SourceContext ctx) {
        Map<String, String> valueMap = new HashMap<>();
        ParameterHolder parameterHolder = new ParameterHolder();

        String host = StringParserUtil
                .getValueWithinDoubleQuotes(ctx.sourceElementValuePairs().host().StringLiteral().getText());
        String port = ctx.sourceElementValuePairs().port().IntegerLiteral().getText();
        String protocol = StringParserUtil
                .getValueWithinDoubleQuotes(ctx.sourceElementValuePairs().protoclo().StringLiteral().getText());

        valueMap.put(Constants.HOST, host);
        valueMap.put(Constants.PORT, port);
        valueMap.put(Constants.PROTOCOL, protocol);
        integration.getAnnotation(ConfigConstants.AN_SOURCE).setValue(valueMap);

        parameterHolder.addParameter(new Parameter(Constants.HOST, host));
        parameterHolder.addParameter(new Parameter(Constants.PORT, port));
        parameterHolder.addParameter(new Parameter(Constants.CONTEXT,
                integration.getAnnotation(ConfigConstants.AN_BASE_PATH).getValue().toString()));

        InboundEndpoint inboundEndpoint = InboundEPProviderRegistry.getInstance().getProvider(protocol)
                .getInboundEndpoint();
        inboundEndpoint.setParameters(parameterHolder);
        integration.addInbound(inboundEndpoint);
    }

    @Override
    public void enterApi(WUMLParser.ApiContext ctx) {
    }

    @Override
    public void exitApi(WUMLParser.ApiContext ctx) {
    }

    @Override
    public void enterResourcePath(WUMLParser.ResourcePathContext ctx) {
    }

    @Override
    public void exitResourcePath(WUMLParser.ResourcePathContext ctx) {
    }

    @Override
    public void enterGetMethod(WUMLParser.GetMethodContext ctx) {
    }

    @Override
    public void exitGetMethod(WUMLParser.GetMethodContext ctx) {
    }

    @Override
    public void enterPostMethod(WUMLParser.PostMethodContext ctx) {
    }

    @Override
    public void exitPostMethod(WUMLParser.PostMethodContext ctx) {
    }

    @Override
    public void enterPutMethod(WUMLParser.PutMethodContext ctx) {
    }

    @Override
    public void exitPutMethod(WUMLParser.PutMethodContext ctx) {
    }

    @Override
    public void enterDeleteMethod(WUMLParser.DeleteMethodContext ctx) {
    }

    @Override
    public void exitDeleteMethod(WUMLParser.DeleteMethodContext ctx) {
    }

    @Override
    public void enterHeadMethod(WUMLParser.HeadMethodContext ctx) {
    }

    @Override
    public void exitHeadMethod(WUMLParser.HeadMethodContext ctx) {
    }

    @Override
    public void enterProdAnt(WUMLParser.ProdAntContext ctx) {
    }

    @Override
    public void exitProdAnt(WUMLParser.ProdAntContext ctx) {
    }

    @Override
    public void enterConAnt(WUMLParser.ConAntContext ctx) {
    }

    @Override
    public void exitConAnt(WUMLParser.ConAntContext ctx) {
    }

    @Override
    public void enterAntApiOperation(WUMLParser.AntApiOperationContext ctx) {
    }

    @Override
    public void exitAntApiOperation(WUMLParser.AntApiOperationContext ctx) {
    }

    @Override
    public void enterAntApiResponses(WUMLParser.AntApiResponsesContext ctx) {
    }

    @Override
    public void exitAntApiResponses(WUMLParser.AntApiResponsesContext ctx) {
    }

    @Override
    public void enterElementValuePairs(WUMLParser.ElementValuePairsContext ctx) {
    }

    @Override
    public void exitElementValuePairs(WUMLParser.ElementValuePairsContext ctx) {
    }

    @Override
    public void enterSourceElementValuePairs(WUMLParser.SourceElementValuePairsContext ctx) {
    }

    @Override
    public void exitSourceElementValuePairs(WUMLParser.SourceElementValuePairsContext ctx) {
    }

    @Override
    public void enterApiElementValuePairs(WUMLParser.ApiElementValuePairsContext ctx) {
    }

    @Override
    public void exitApiElementValuePairs(WUMLParser.ApiElementValuePairsContext ctx) {
    }

    @Override
    public void enterProtoclo(WUMLParser.ProtocloContext ctx) {
    }

    @Override
    public void exitProtoclo(WUMLParser.ProtocloContext ctx) {
    }

    @Override
    public void enterHost(WUMLParser.HostContext ctx) {
    }

    @Override
    public void exitHost(WUMLParser.HostContext ctx) {
    }

    @Override
    public void enterPort(WUMLParser.PortContext ctx) {
    }

    @Override
    public void exitPort(WUMLParser.PortContext ctx) {
    }

    @Override
    public void enterTags(WUMLParser.TagsContext ctx) {
    }

    @Override
    public void exitTags(WUMLParser.TagsContext ctx) {
    }

    @Override
    public void enterTag(WUMLParser.TagContext ctx) {
    }

    @Override
    public void exitTag(WUMLParser.TagContext ctx) {
    }

    @Override
    public void enterDescripton(WUMLParser.DescriptonContext ctx) {
    }

    @Override
    public void exitDescripton(WUMLParser.DescriptonContext ctx) {
    }

    @Override
    public void enterProducer(WUMLParser.ProducerContext ctx) {
    }

    @Override
    public void exitProducer(WUMLParser.ProducerContext ctx) {
    }

    @Override
    public void enterConstant(WUMLParser.ConstantContext ctx) {
    }

    @Override
    public void exitConstant(WUMLParser.ConstantContext ctx) {
        String type = (ctx.classType() != null) ? ctx.classType().getText() : ctx.type().getText();
        /* Extracting endpoints as constants */
        if (Constants.ENDPOINT.equals(type)) {
            String endpointType = ctx.Identifier().getText();

            OutboundEndpoint outboundEndpoint = OutboundEPProviderRegistry.getInstance().getProvider(
                    endpointType.replace(Constants.ENDPOINT_GRAMMAR_KEYWORD, Constants.EMPTY_STRING)
                            .toLowerCase(Locale.ENGLISH)).getEndpoint();
            outboundEndpoint.setName(ctx.variableDeclaratorId().getText());
            outboundEndpoint.setUri(StringParserUtil.getValueWithinDoubleQuotes(ctx.StringLiteral().getText()));
            integration.getOutbounds().put(ctx.variableDeclaratorId().getText(), outboundEndpoint);
        }
    }

    @Override
    public void enterResource(WUMLParser.ResourceContext ctx) {
    }

    @Override
    public void exitResource(WUMLParser.ResourceContext ctx) {

        String path = StringParserUtil.getValueWithinDoubleQuotes(ctx.resourcePath().StringLiteral().getText());

        URITemplate uriTemplate = null;

        try {
            uriTemplate = new URITemplate(path);
        } catch (URITemplateException e) {
            log.error("Unable to create URI template for :" + path);
        }

        /* Extracting the resource name */
        Resource resource = new Resource(
                ((WUMLParser.ResourceContext) ctx).resourceDeclaration().Identifier().get(0).getText(), uriTemplate);

        /* Updating annotations */
        if (!ctx.httpMethods().getMethod().isEmpty()) {
            resource.getAnnotations().get(ConfigConstants.GET_ANNOTATION).setValue(Boolean.TRUE);
        }
        if (!ctx.httpMethods().putMethod().isEmpty()) {
            resource.getAnnotations().get(ConfigConstants.PUT_ANNOTATION).setValue(Boolean.TRUE);
        }
        if (!ctx.httpMethods().postMethod().isEmpty()) {
            resource.getAnnotations().get(ConfigConstants.POST_ANNOTATION).setValue(Boolean.TRUE);
        }
        if (!ctx.httpMethods().deleteMethod().isEmpty()) {
            resource.getAnnotations().get(ConfigConstants.DELETE_ANNOTATION).setValue(Boolean.TRUE);
        }
        resource.getAnnotations().get(ConfigConstants.AN_BASE_PATH).setValue(path);

        /* Processing blocks inside the resource */
        for (WUMLParser.BlockStatementContext blockStatement : ctx.resourceDeclaration().block().blockStatement()) {
            if (blockStatement.getChild(0) instanceof WUMLParser.LocalVariableDeclarationStatementContext) {
            } else if (blockStatement.getChild(0) instanceof WUMLParser.StatementExpressionContext) {
                WUMLParser.StatementExpressionContext statementExpression =
                        (WUMLParser.StatementExpressionContext) blockStatement.getChild(0);
                if ("reply".equals(statementExpression.getChild(0).getText())) {
                    if ("invoke".equals(statementExpression.getChild(1).getChild(0).getText())) {
                        //add the call mediator
                        Mediator callMediator = MediatorProviderRegistry.getInstance().getMediator("call");

                        ParameterHolder parameterHolder = new ParameterHolder();
                        parameterHolder.addParameter(new Parameter("endpointKey", statementExpression.getChild(1)
                                .getChild(2).getChild(0).getText()));

                        callMediator.setParameters(parameterHolder);

                        resource.getDefaultWorker().addMediator(callMediator);

                    }
                }

            }
        }

        integration.getResources().put(resource.getName(), resource);
    }

    @Override
    public void enterHttpMethods(WUMLParser.HttpMethodsContext ctx) {
    }

    @Override
    public void exitHttpMethods(WUMLParser.HttpMethodsContext ctx) {
    }

    @Override
    public void enterQualifiedName(WUMLParser.QualifiedNameContext ctx) {
    }

    @Override
    public void exitQualifiedName(WUMLParser.QualifiedNameContext ctx) {
    }

    @Override
    public void enterResourceDeclaration(WUMLParser.ResourceDeclarationContext ctx) {
    }

    @Override
    public void exitResourceDeclaration(WUMLParser.ResourceDeclarationContext ctx) {
    }

    @Override
    public void enterElementValuePair(WUMLParser.ElementValuePairContext ctx) {
    }

    @Override
    public void exitElementValuePair(WUMLParser.ElementValuePairContext ctx) {
    }

    @Override
    public void enterElementValue(WUMLParser.ElementValueContext ctx) {
    }

    @Override
    public void exitElementValue(WUMLParser.ElementValueContext ctx) {
    }

    @Override
    public void enterBlock(WUMLParser.BlockContext ctx) {
    }

    @Override
    public void exitBlock(WUMLParser.BlockContext ctx) {
    }

    @Override
    public void enterBlockStatement(WUMLParser.BlockStatementContext ctx) {
    }

    @Override
    public void exitBlockStatement(WUMLParser.BlockStatementContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void enterTryBlock(WUMLParser.TryBlockContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void exitTryBlock(WUMLParser.TryBlockContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void enterIfBlock(WUMLParser.IfBlockContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void exitIfBlock(WUMLParser.IfBlockContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void enterStatement(WUMLParser.StatementContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void exitStatement(WUMLParser.StatementContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void enterStatementExpression(WUMLParser.StatementExpressionContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void exitStatementExpression(WUMLParser.StatementExpressionContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void enterParExpression(WUMLParser.ParExpressionContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void exitParExpression(WUMLParser.ParExpressionContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void enterExpressionList(WUMLParser.ExpressionListContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void exitExpressionList(WUMLParser.ExpressionListContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void enterCatchClause(WUMLParser.CatchClauseContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void exitCatchClause(WUMLParser.CatchClauseContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void enterCatchType(WUMLParser.CatchTypeContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void exitCatchType(WUMLParser.CatchTypeContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void enterFinallyBlock(WUMLParser.FinallyBlockContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void exitFinallyBlock(WUMLParser.FinallyBlockContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void enterLocalVariableDeclarationStatement(WUMLParser.LocalVariableDeclarationStatementContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void exitLocalVariableDeclarationStatement(WUMLParser.LocalVariableDeclarationStatementContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void enterVariableDeclarator(WUMLParser.VariableDeclaratorContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void exitVariableDeclarator(WUMLParser.VariableDeclaratorContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void enterVariableDeclaratorId(WUMLParser.VariableDeclaratorIdContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void exitVariableDeclaratorId(WUMLParser.VariableDeclaratorIdContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void enterExpression(WUMLParser.ExpressionContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void exitExpression(WUMLParser.ExpressionContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void enterPrimary(WUMLParser.PrimaryContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void exitPrimary(WUMLParser.PrimaryContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void enterLiteral(WUMLParser.LiteralContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void exitLiteral(WUMLParser.LiteralContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void enterMethodParams(WUMLParser.MethodParamsContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void exitMethodParams(WUMLParser.MethodParamsContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void enterType(WUMLParser.TypeContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void exitType(WUMLParser.TypeContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void enterClassType(WUMLParser.ClassTypeContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void exitClassType(WUMLParser.ClassTypeContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void enterMediaType(WUMLParser.MediaTypeContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void exitMediaType(WUMLParser.MediaTypeContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void visitTerminal(TerminalNode node) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void visitErrorNode(ErrorNode node) {
    }

}
