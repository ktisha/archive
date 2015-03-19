<?xml version="1.0" encoding="UTF-8"?>
<model modelUID="r:7fdffddf-56e0-4217-88f1-affa4daf15e7(tuzova.finiteStateMachine.generator.template.main@generator)">
  <persistence version="3" />
  <refactoringHistory />
  <language namespace="b401a680-8325-4110-8fd3-84331ff25bef(jetbrains.mps.lang.generator)" />
  <language namespace="d7706f63-9be2-479c-a3da-ae92af1e64d5(jetbrains.mps.lang.generator.generationContext)" />
  <language namespace="f3061a53-9226-4cc5-a443-f952ceaf5816(jetbrains.mps.baseLanguage)" />
  <language namespace="00d66c09-c182-4747-84e3-58794ed31fa9(tuzova.finiteStateMachine)" />
  <language namespace="ceab5195-25ea-4f22-9b92-103b95ca8c0c(jetbrains.mps.lang.core)" />
  <language namespace="83888646-71ce-4f1c-9c53-c54016f6ad4f(jetbrains.mps.baseLanguage.collections)" />
  <language namespace="13744753-c81f-424a-9c1b-cf8943bf4e86(jetbrains.mps.lang.sharedConcepts)" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c895902fb(jetbrains.mps.lang.smodel.constraints)" version="21" />
  <languageAspect modelUID="r:309aeee7-bee8-445c-b31d-35928d1da75f(jetbrains.mps.baseLanguage.tuples.structure)" version="2" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c89590338(jetbrains.mps.baseLanguage.closures.structure)" version="3" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c895902db(jetbrains.mps.baseLanguage.blTypes.constraints)" version="0" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c895902f3(jetbrains.mps.lang.generator.generationContext.structure)" version="0" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c8959028c(jetbrains.mps.lang.structure.constraints)" version="11" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c89590283(jetbrains.mps.lang.core.constraints)" version="2" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c895902ba(jetbrains.mps.lang.sharedConcepts.constraints)" version="0" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c895902e8(jetbrains.mps.lang.generator.structure)" version="2" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c895902c1(jetbrains.mps.baseLanguage.constraints)" version="83" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c89590292(jetbrains.mps.lang.structure.structure)" version="0" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c89590334(jetbrains.mps.baseLanguage.closures.constraints)" version="2" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c89590301(jetbrains.mps.lang.smodel.structure)" version="16" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c895902bc(jetbrains.mps.lang.sharedConcepts.structure)" version="0" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c895902e2(jetbrains.mps.lang.generator.constraints)" version="16" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c895902ca(jetbrains.mps.baseLanguage.structure)" version="1" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c89590288(jetbrains.mps.lang.core.structure)" version="0" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c8959033d(jetbrains.mps.lang.annotations.structure)" version="0" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c895902ae(jetbrains.mps.lang.typesystem.constraints)" version="17" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c89590340(jetbrains.mps.lang.pattern.constraints)" version="2" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c89590345(jetbrains.mps.lang.pattern.structure)" version="0" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c89590328(jetbrains.mps.baseLanguage.collections.constraints)" version="6" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c895902b4(jetbrains.mps.lang.typesystem.structure)" version="3" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c8959034b(jetbrains.mps.lang.quotation.structure)" version="0" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c8959032e(jetbrains.mps.baseLanguage.collections.structure)" version="7" />
  <devkit namespace="2677cb18-f558-4e33-bc38-a5139cee06dc(jetbrains.mps.devkit.language-design)" />
  <maxImportIndex value="2" />
  <import index="1" modelUID="r:de69fd3c-5806-478d-b028-6c2c64d68039(tuzova.finiteStateMachine.structure)" version="-1" />
  <import index="2" modelUID="f:java_stub#java.lang(java.lang@java_stub)" version="-1" />
  <node type="jetbrains.mps.lang.generator.structure.MappingConfiguration" id="797427956580460029">
    <property name="name" value="main" />
    <node role="rootMappingRule" type="jetbrains.mps.lang.generator.structure.Root_MappingRule" id="797427956580468737">
      <link role="applicableConcept" targetNodeId="1.797427956577465754" resolveInfo="FiniteStateMachine" />
      <link role="template" targetNodeId="797427956580460030" resolveInfo="FSMachineImpl" />
    </node>
    <node role="rootMappingRule" type="jetbrains.mps.lang.generator.structure.Root_MappingRule" id="797427956581251599">
      <link role="template" targetNodeId="326387346505967490" resolveInfo="Event" />
      <link role="applicableConcept" targetNodeId="1.797427956580782756" resolveInfo="EventContainer" />
    </node>
    <node role="rootMappingRule" type="jetbrains.mps.lang.generator.structure.Root_MappingRule" id="797427956581994499">
      <link role="template" targetNodeId="797427956581889117" resolveInfo="States" />
      <link role="applicableConcept" targetNodeId="1.797427956577465754" resolveInfo="FiniteStateMachine" />
    </node>
  </node>
  <visible index="2" modelUID="r:00000000-0000-4000-0000-011c89590288(jetbrains.mps.lang.core.structure)" />
  <node type="jetbrains.mps.baseLanguage.structure.ClassConcept" id="797427956580460030">
    <property name="name" value="FSMachineImpl" />
    <node role="method" type="jetbrains.mps.baseLanguage.structure.InstanceMethodDeclaration" id="797427956582298919">
      <property name="name" value="eval" />
      <node role="visibility" type="jetbrains.mps.baseLanguage.structure.PublicVisibility" id="797427956582298921" />
      <node role="body" type="jetbrains.mps.baseLanguage.structure.StatementList" id="797427956582298922">
        <node role="statement" type="jetbrains.mps.baseLanguage.structure.ExpressionStatement" id="4946881345607050721">
          <node role="expression" type="jetbrains.mps.baseLanguage.structure.AssignmentExpression" id="4946881345607050722">
            <node role="rValue" type="jetbrains.mps.baseLanguage.structure.ParameterReference" id="4946881345607050723">
              <link role="variableDeclaration" targetNodeId="797427956583379571" resolveInfo="init" />
            </node>
            <node role="lValue" type="jetbrains.mps.baseLanguage.structure.DotExpression" id="4946881345607050724">
              <node role="operation" type="jetbrains.mps.baseLanguage.structure.FieldReferenceOperation" id="4946881345607050725">
                <link role="fieldDeclaration" targetNodeId="797427956582194662" resolveInfo="myCurrentState" />
              </node>
              <node role="operand" type="jetbrains.mps.baseLanguage.structure.ThisExpression" id="4946881345607050726" />
            </node>
          </node>
        </node>
        <node role="statement" type="jetbrains.mps.baseLanguage.structure.LocalVariableDeclarationStatement" id="797427956582301544">
          <node role="localVariableDeclaration" type="jetbrains.mps.baseLanguage.structure.LocalVariableDeclaration" id="797427956582301545">
            <property name="name" value="event" />
            <node role="type" type="jetbrains.mps.baseLanguage.structure.ClassifierType" id="797427956582301546">
              <link role="classifier" targetNodeId="326387346505967490" resolveInfo="Event" />
            </node>
          </node>
        </node>
        <node role="statement" type="jetbrains.mps.baseLanguage.structure.ForStatement" id="4946881345607156104">
          <node role="body" type="jetbrains.mps.baseLanguage.structure.StatementList" id="4946881345607156105">
            <node role="statement" type="jetbrains.mps.baseLanguage.structure.ExpressionStatement" id="4946881345607156125">
              <node role="expression" type="jetbrains.mps.baseLanguage.structure.AssignmentExpression" id="4946881345607156126">
                <node role="lValue" type="jetbrains.mps.baseLanguage.structure.LocalVariableReference" id="4946881345607156127">
                  <link role="variableDeclaration" targetNodeId="797427956582301545" resolveInfo="event" />
                </node>
                <node role="rValue" type="jetbrains.mps.baseLanguage.structure.ArrayAccessExpression" id="4946881345607156128">
                  <node role="array" type="jetbrains.mps.baseLanguage.structure.ParameterReference" id="4946881345607156129">
                    <link role="variableDeclaration" targetNodeId="797427956583379573" resolveInfo="events" />
                  </node>
                  <node role="index" type="jetbrains.mps.baseLanguage.structure.LocalVariableReference" id="4946881345607156130">
                    <link role="variableDeclaration" targetNodeId="4946881345607156107" resolveInfo="i" />
                  </node>
                </node>
              </node>
            </node>
            <node role="statement" type="jetbrains.mps.baseLanguage.structure.SwitchStatement" id="4946881345607156131">
              <node role="expression" type="jetbrains.mps.baseLanguage.structure.DotExpression" id="4946881345607156132">
                <node role="operation" type="jetbrains.mps.baseLanguage.structure.FieldReferenceOperation" id="4946881345607156133">
                  <link role="fieldDeclaration" targetNodeId="797427956582194662" resolveInfo="myCurrentState" />
                </node>
                <node role="operand" type="jetbrains.mps.baseLanguage.structure.ThisExpression" id="4946881345607156134" />
              </node>
              <node role="defaultBlock" type="jetbrains.mps.baseLanguage.structure.StatementList" id="4946881345607156135">
                <node role="statement" type="jetbrains.mps.baseLanguage.structure.BreakStatement" id="4946881345607156136" />
              </node>
              <node role="case" type="jetbrains.mps.baseLanguage.structure.SwitchCase" id="4946881345607156137">
                <node role="body" type="jetbrains.mps.baseLanguage.structure.StatementList" id="4946881345607156138">
                  <node role="statement" type="jetbrains.mps.baseLanguage.structure.SwitchStatement" id="4946881345607156153">
                    <node role="expression" type="jetbrains.mps.baseLanguage.structure.LocalVariableReference" id="4946881345607156154">
                      <link role="variableDeclaration" targetNodeId="797427956582301545" resolveInfo="event" />
                    </node>
                    <node role="case" type="jetbrains.mps.baseLanguage.structure.SwitchCase" id="4946881345607156155">
                      <node role="body" type="jetbrains.mps.baseLanguage.structure.StatementList" id="4946881345607156156">
                        <node role="statement" type="jetbrains.mps.baseLanguage.structure.ExpressionStatement" id="4946881345607365578">
                          <node role="expression" type="jetbrains.mps.baseLanguage.structure.AssignmentExpression" id="4946881345607365579">
                            <node role="lValue" type="jetbrains.mps.baseLanguage.structure.DotExpression" id="4946881345607365580">
                              <node role="operation" type="jetbrains.mps.baseLanguage.structure.FieldReferenceOperation" id="4946881345607365581">
                                <link role="fieldDeclaration" targetNodeId="797427956582194662" resolveInfo="myCurrentState" />
                              </node>
                              <node role="operand" type="jetbrains.mps.baseLanguage.structure.ThisExpression" id="4946881345607365582" />
                            </node>
                            <node role="rValue" type="jetbrains.mps.baseLanguage.structure.EnumConstantReference" id="4946881345607365583">
                              <link role="enumClass" targetNodeId="797427956581889117" resolveInfo="State" />
                              <link role="enumConstantDeclaration" targetNodeId="797427956581992625" resolveInfo="name" />
                              <node role="nodeMacro$attribute" type="jetbrains.mps.lang.generator.structure.LoopMacro" id="4946881345607365584">
                                <node role="sourceNodesQuery" type="jetbrains.mps.lang.generator.structure.SourceSubstituteMacro_SourceNodesQuery" id="4946881345607365585">
                                  <node role="body" type="jetbrains.mps.baseLanguage.structure.StatementList" id="4946881345607365586">
                                    <node role="statement" type="jetbrains.mps.baseLanguage.structure.ExpressionStatement" id="4946881345607365587">
                                      <node role="expression" type="jetbrains.mps.baseLanguage.structure.DotExpression" id="4946881345607365588">
                                        <node role="operand" type="jetbrains.mps.lang.generator.structure.TemplateFunctionParameter_sourceNode" id="4946881345607365589" />
                                        <node role="operation" type="jetbrains.mps.lang.smodel.structure.SLinkListAccess" id="4946881345607365590">
                                          <link role="link" targetNodeId="1.326387346505526625" />
                                        </node>
                                      </node>
                                    </node>
                                  </node>
                                </node>
                              </node>
                              <node role="referenceMacro$link_attribute$enumConstantDeclaration" type="jetbrains.mps.lang.generator.structure.ReferenceMacro" id="4946881345607365591">
                                <node role="referentFunction" type="jetbrains.mps.lang.generator.structure.ReferenceMacro_GetReferent" id="4946881345607365592">
                                  <node role="body" type="jetbrains.mps.baseLanguage.structure.StatementList" id="4946881345607365593">
                                    <node role="statement" type="jetbrains.mps.baseLanguage.structure.ExpressionStatement" id="4946881345607365594">
                                      <node role="expression" type="jetbrains.mps.baseLanguage.structure.DotExpression" id="4946881345607365595">
                                        <node role="operand" type="jetbrains.mps.lang.generator.structure.TemplateFunctionParameter_sourceNode" id="4946881345607365596" />
                                        <node role="operation" type="jetbrains.mps.lang.smodel.structure.SPropertyAccess" id="4946881345607365597">
                                          <link role="property" targetNodeId="2v.1169194664001" resolveInfo="name" />
                                        </node>
                                      </node>
                                    </node>
                                  </node>
                                </node>
                              </node>
                            </node>
                          </node>
                        </node>
                        <node role="statement" type="jetbrains.mps.baseLanguage.structure.BreakStatement" id="4946881345607365576" />
                      </node>
                      <node role="expression" type="jetbrains.mps.baseLanguage.structure.EnumConstantReference" id="4946881345607156177">
                        <link role="enumClass" targetNodeId="326387346505967490" resolveInfo="Event" />
                        <link role="enumConstantDeclaration" targetNodeId="326387346505967497" resolveInfo="name" />
                        <node role="nodeMacro$attribute" type="jetbrains.mps.lang.generator.structure.LoopMacro" id="4946881345607156178">
                          <node role="sourceNodesQuery" type="jetbrains.mps.lang.generator.structure.SourceSubstituteMacro_SourceNodesQuery" id="4946881345607156179">
                            <node role="body" type="jetbrains.mps.baseLanguage.structure.StatementList" id="4946881345607156180">
                              <node role="statement" type="jetbrains.mps.baseLanguage.structure.ExpressionStatement" id="4946881345607156181">
                                <node role="expression" type="jetbrains.mps.baseLanguage.structure.DotExpression" id="4946881345607156182">
                                  <node role="operand" type="jetbrains.mps.lang.generator.structure.TemplateFunctionParameter_sourceNode" id="4946881345607156183" />
                                  <node role="operation" type="jetbrains.mps.lang.smodel.structure.SLinkListAccess" id="4946881345607156184">
                                    <link role="link" targetNodeId="1.326387346505526624" />
                                  </node>
                                </node>
                              </node>
                            </node>
                          </node>
                        </node>
                        <node role="referenceMacro$link_attribute$enumConstantDeclaration" type="jetbrains.mps.lang.generator.structure.ReferenceMacro" id="4946881345607156185">
                          <node role="referentFunction" type="jetbrains.mps.lang.generator.structure.ReferenceMacro_GetReferent" id="4946881345607156186">
                            <node role="body" type="jetbrains.mps.baseLanguage.structure.StatementList" id="4946881345607156187">
                              <node role="statement" type="jetbrains.mps.baseLanguage.structure.ExpressionStatement" id="4946881345607156188">
                                <node role="expression" type="jetbrains.mps.baseLanguage.structure.DotExpression" id="4946881345607156189">
                                  <node role="operand" type="jetbrains.mps.lang.generator.structure.TemplateFunctionParameter_sourceNode" id="4946881345607156190" />
                                  <node role="operation" type="jetbrains.mps.lang.smodel.structure.SPropertyAccess" id="4946881345607156191">
                                    <link role="property" targetNodeId="2v.1169194664001" resolveInfo="name" />
                                  </node>
                                </node>
                              </node>
                            </node>
                          </node>
                        </node>
                      </node>
                      <node role="nodeMacro$attribute" type="jetbrains.mps.lang.generator.structure.LoopMacro" id="4946881345607156192">
                        <node role="sourceNodesQuery" type="jetbrains.mps.lang.generator.structure.SourceSubstituteMacro_SourceNodesQuery" id="4946881345607156193">
                          <node role="body" type="jetbrains.mps.baseLanguage.structure.StatementList" id="4946881345607156194">
                            <node role="statement" type="jetbrains.mps.baseLanguage.structure.ExpressionStatement" id="4946881345607156195">
                              <node role="expression" type="jetbrains.mps.baseLanguage.structure.DotExpression" id="4946881345607156196">
                                <node role="operand" type="jetbrains.mps.lang.generator.structure.TemplateFunctionParameter_sourceNode" id="4946881345607156197" />
                                <node role="operation" type="jetbrains.mps.lang.smodel.structure.SLinkListAccess" id="4946881345607156198">
                                  <link role="link" targetNodeId="1.797427956579812124" />
                                </node>
                              </node>
                            </node>
                          </node>
                        </node>
                      </node>
                    </node>
                    <node role="defaultBlock" type="jetbrains.mps.baseLanguage.structure.StatementList" id="4946881345607156199">
                      <node role="statement" type="jetbrains.mps.baseLanguage.structure.BreakStatement" id="4946881345607156200" />
                    </node>
                  </node>
                </node>
                <node role="nodeMacro$attribute" type="jetbrains.mps.lang.generator.structure.LoopMacro" id="4946881345607156201">
                  <node role="sourceNodesQuery" type="jetbrains.mps.lang.generator.structure.SourceSubstituteMacro_SourceNodesQuery" id="4946881345607156202">
                    <node role="body" type="jetbrains.mps.baseLanguage.structure.StatementList" id="4946881345607156203">
                      <node role="statement" type="jetbrains.mps.baseLanguage.structure.ExpressionStatement" id="4946881345607156204">
                        <node role="expression" type="jetbrains.mps.baseLanguage.structure.DotExpression" id="4946881345607156205">
                          <node role="operand" type="jetbrains.mps.lang.generator.structure.TemplateFunctionParameter_sourceNode" id="4946881345607156206" />
                          <node role="operation" type="jetbrains.mps.lang.smodel.structure.SLinkListAccess" id="4946881345607156207">
                            <link role="link" targetNodeId="1.797427956578312507" />
                          </node>
                        </node>
                      </node>
                    </node>
                  </node>
                </node>
                <node role="expression" type="jetbrains.mps.baseLanguage.structure.EnumConstantReference" id="4946881345607156208">
                  <link role="enumClass" targetNodeId="797427956581889117" resolveInfo="State" />
                  <link role="enumConstantDeclaration" targetNodeId="797427956581992625" resolveInfo="name" />
                </node>
              </node>
            </node>
          </node>
          <node role="variable" type="jetbrains.mps.baseLanguage.structure.LocalVariableDeclaration" id="4946881345607156107">
            <property name="name" value="i" />
            <node role="type" type="jetbrains.mps.baseLanguage.structure.IntegerType" id="4946881345607156109" />
            <node role="initializer" type="jetbrains.mps.baseLanguage.structure.IntegerConstant" id="4946881345607156111">
              <property name="value" value="0" />
            </node>
          </node>
          <node role="condition" type="jetbrains.mps.baseLanguage.structure.NotEqualsExpression" id="4946881345607156113">
            <node role="rightExpression" type="jetbrains.mps.baseLanguage.structure.DotExpression" id="4946881345607156117">
              <node role="operand" type="jetbrains.mps.baseLanguage.structure.ParameterReference" id="4946881345607156116">
                <link role="variableDeclaration" targetNodeId="797427956583379573" resolveInfo="events" />
              </node>
              <node role="operation" type="jetbrains.mps.baseLanguage.structure.ArrayLengthOperation" id="4946881345607156121" />
            </node>
            <node role="leftExpression" type="jetbrains.mps.baseLanguage.structure.LocalVariableReference" id="4946881345607156112">
              <link role="variableDeclaration" targetNodeId="4946881345607156107" resolveInfo="i" />
            </node>
          </node>
          <node role="iteration" type="jetbrains.mps.baseLanguage.structure.PrefixIncrementExpression" id="4946881345607156122">
            <node role="expression" type="jetbrains.mps.baseLanguage.structure.LocalVariableReference" id="4946881345607156124">
              <link role="variableDeclaration" targetNodeId="4946881345607156107" resolveInfo="i" />
            </node>
          </node>
        </node>
        <node role="statement" type="jetbrains.mps.baseLanguage.structure.ReturnStatement" id="4946881345607156086">
          <node role="expression" type="jetbrains.mps.baseLanguage.structure.DotExpression" id="4946881345607156088">
            <node role="operation" type="jetbrains.mps.baseLanguage.structure.FieldReferenceOperation" id="4946881345607156089">
              <link role="fieldDeclaration" targetNodeId="797427956582194662" resolveInfo="myCurrentState" />
            </node>
            <node role="operand" type="jetbrains.mps.baseLanguage.structure.ThisExpression" id="4946881345607156090" />
          </node>
        </node>
      </node>
      <node role="returnType" type="jetbrains.mps.baseLanguage.structure.ClassifierType" id="797427956582301539">
        <link role="classifier" targetNodeId="797427956581889117" resolveInfo="State" />
      </node>
      <node role="parameter" type="jetbrains.mps.baseLanguage.structure.ParameterDeclaration" id="797427956583379571">
        <property name="name" value="init" />
        <node role="type" type="jetbrains.mps.baseLanguage.structure.ClassifierType" id="797427956583379572">
          <link role="classifier" targetNodeId="797427956581889117" resolveInfo="State" />
        </node>
      </node>
      <node role="parameter" type="jetbrains.mps.baseLanguage.structure.ParameterDeclaration" id="797427956583379573">
        <property name="name" value="events" />
        <node role="type" type="jetbrains.mps.baseLanguage.structure.ArrayType" id="797427956583379578">
          <node role="componentType" type="jetbrains.mps.baseLanguage.structure.ClassifierType" id="797427956583379579">
            <link role="classifier" targetNodeId="326387346505967490" resolveInfo="Event" />
          </node>
        </node>
      </node>
    </node>
    <node role="field" type="jetbrains.mps.baseLanguage.structure.FieldDeclaration" id="797427956582194662">
      <property name="name" value="myCurrentState" />
      <node role="visibility" type="jetbrains.mps.baseLanguage.structure.PrivateVisibility" id="797427956582194663" />
      <node role="type" type="jetbrains.mps.baseLanguage.structure.ClassifierType" id="797427956582197281">
        <link role="classifier" targetNodeId="797427956581889117" resolveInfo="States" />
      </node>
    </node>
    <node role="staticInnerClassifiers" type="jetbrains.mps.baseLanguage.structure.Classifier" id="797427956581363645">
      <node role="visibility" type="jetbrains.mps.baseLanguage.structure.PublicVisibility" id="797427956581363646" />
    </node>
    <node role="visibility" type="jetbrains.mps.baseLanguage.structure.PublicVisibility" id="797427956580460031" />
    <node role="constructor" type="jetbrains.mps.baseLanguage.structure.ConstructorDeclaration" id="797427956580460032">
      <node role="returnType" type="jetbrains.mps.baseLanguage.structure.VoidType" id="797427956580460033" />
      <node role="visibility" type="jetbrains.mps.baseLanguage.structure.PublicVisibility" id="797427956580460034" />
      <node role="body" type="jetbrains.mps.baseLanguage.structure.StatementList" id="797427956580460035" />
    </node>
    <node role="rootTemplateAnnotation$attribute" type="jetbrains.mps.lang.generator.structure.RootTemplateAnnotation" id="797427956580460036">
      <link role="applicableConcept" targetNodeId="1.797427956577465754" resolveInfo="FiniteStateMachine" />
    </node>
    <node role="propertyMacro$property_attribute$name" type="jetbrains.mps.lang.generator.structure.PropertyMacro" id="797427956580670555">
      <node role="propertyValueFunction" type="jetbrains.mps.lang.generator.structure.PropertyMacro_GetPropertyValue" id="797427956580670558">
        <node role="body" type="jetbrains.mps.baseLanguage.structure.StatementList" id="797427956580670559">
          <node role="statement" type="jetbrains.mps.baseLanguage.structure.ExpressionStatement" id="797427956580670560">
            <node role="expression" type="jetbrains.mps.baseLanguage.structure.DotExpression" id="797427956580670561">
              <node role="operation" type="jetbrains.mps.lang.smodel.structure.SPropertyAccess" id="797427956580670562">
                <link role="property" targetNodeId="2v.1169194664001" resolveInfo="name" />
              </node>
              <node role="operand" type="jetbrains.mps.lang.generator.structure.TemplateFunctionParameter_sourceNode" id="797427956580670563" />
            </node>
          </node>
        </node>
      </node>
    </node>
    <node role="staticMethod" type="jetbrains.mps.baseLanguage.structure.StaticMethodDeclaration" id="4946881345607365563">
      <property name="name" value="main" />
      <property name="isFinal" value="false" />
      <node role="returnType" type="jetbrains.mps.baseLanguage.structure.VoidType" id="4946881345607362925" />
      <node role="body" type="jetbrains.mps.baseLanguage.structure.StatementList" id="4946881345607362927">
        <node role="statement" type="jetbrains.mps.baseLanguage.structure.LocalVariableDeclarationStatement" id="4946881345607928211">
          <node role="localVariableDeclaration" type="jetbrains.mps.baseLanguage.structure.LocalVariableDeclaration" id="4946881345607928212">
            <property name="name" value="machine" />
            <node role="type" type="jetbrains.mps.baseLanguage.structure.ClassifierType" id="4946881345607928213">
              <link role="classifier" targetNodeId="797427956580460030" resolveInfo="FSMachineImpl" />
            </node>
            <node role="initializer" type="jetbrains.mps.baseLanguage.structure.GenericNewExpression" id="4946881345607928216">
              <node role="creator" type="jetbrains.mps.baseLanguage.structure.ClassCreator" id="4946881345607938080">
                <link role="baseMethodDeclaration" targetNodeId="797427956580460032" resolveInfo="FSMachineImpl" />
              </node>
            </node>
          </node>
        </node>
        <node role="statement" type="jetbrains.mps.baseLanguage.structure.LocalVariableDeclarationStatement" id="4946881345607938081">
          <node role="localVariableDeclaration" type="jetbrains.mps.baseLanguage.structure.LocalVariableDeclaration" id="4946881345607938082">
            <property name="name" value="events" />
            <node role="type" type="jetbrains.mps.baseLanguage.structure.ArrayType" id="4946881345607938084">
              <node role="componentType" type="jetbrains.mps.baseLanguage.structure.ClassifierType" id="4946881345607938085">
                <link role="classifier" targetNodeId="326387346505967490" resolveInfo="Event" />
              </node>
            </node>
            <node role="initializer" type="jetbrains.mps.baseLanguage.structure.GenericNewExpression" id="4946881345607938091">
              <node role="creator" type="jetbrains.mps.baseLanguage.structure.ArrayCreator" id="4946881345607938092">
                <node role="dimensionExpression" type="jetbrains.mps.baseLanguage.structure.DimensionExpression" id="4946881345607938093">
                  <node role="expression" type="jetbrains.mps.baseLanguage.structure.IntegerConstant" id="4946881345608266316">
                    <property name="value" value="0" />
                    <node role="propertyMacro$property_attribute$value" type="jetbrains.mps.lang.generator.structure.PropertyMacro" id="4946881345608266318">
                      <node role="propertyValueFunction" type="jetbrains.mps.lang.generator.structure.PropertyMacro_GetPropertyValue" id="4946881345608266319">
                        <node role="body" type="jetbrains.mps.baseLanguage.structure.StatementList" id="4946881345608266320">
                          <node role="statement" type="jetbrains.mps.baseLanguage.structure.ExpressionStatement" id="4946881345608266321">
                            <node role="expression" type="jetbrains.mps.baseLanguage.structure.DotExpression" id="4946881345608266333">
                              <node role="operand" type="jetbrains.mps.baseLanguage.structure.DotExpression" id="4946881345608266328">
                                <node role="operand" type="jetbrains.mps.baseLanguage.structure.DotExpression" id="4946881345608266323">
                                  <node role="operand" type="jetbrains.mps.lang.generator.structure.TemplateFunctionParameter_sourceNode" id="4946881345608266322" />
                                  <node role="operation" type="jetbrains.mps.lang.smodel.structure.SLinkAccess" id="4946881345608266327">
                                    <link role="link" targetNodeId="1.4946881345607938099" />
                                  </node>
                                </node>
                                <node role="operation" type="jetbrains.mps.lang.smodel.structure.Node_GetChildrenOperation" id="4946881345608266332" />
                              </node>
                              <node role="operation" type="jetbrains.mps.baseLanguage.collections.structure.GetSizeOperation" id="4946881345608266337" />
                            </node>
                          </node>
                        </node>
                      </node>
                    </node>
                  </node>
                </node>
                <node role="componentType" type="jetbrains.mps.baseLanguage.structure.ClassifierType" id="4946881345607938095">
                  <link role="classifier" targetNodeId="326387346505967490" resolveInfo="Event" />
                </node>
              </node>
            </node>
          </node>
        </node>
        <node role="statement" type="jetbrains.mps.baseLanguage.structure.Statement" id="4946881345608371006" />
      </node>
      <node role="visibility" type="jetbrains.mps.baseLanguage.structure.PublicVisibility" id="4946881345607362926" />
      <node role="parameter" type="jetbrains.mps.baseLanguage.structure.ParameterDeclaration" id="4946881345607690796">
        <property name="name" value="args" />
        <node role="type" type="jetbrains.mps.baseLanguage.structure.ArrayType" id="4946881345607690799">
          <node role="componentType" type="jetbrains.mps.baseLanguage.structure.ClassifierType" id="4946881345607690800">
            <link role="classifier" targetNodeId="2.~String" resolveInfo="String" />
          </node>
        </node>
      </node>
    </node>
  </node>
  <node type="jetbrains.mps.baseLanguage.structure.EnumClass" id="797427956581889117">
    <property name="name" value="State" />
    <node role="enumConstant" type="jetbrains.mps.baseLanguage.structure.EnumConstantDeclaration" id="797427956581992625">
      <property name="name" value="name" />
      <link role="baseMethodDeclaration" targetNodeId="797427956581889119" resolveInfo="States" />
      <node role="nodeMacro$attribute" type="jetbrains.mps.lang.generator.structure.LoopMacro" id="797427956581992627">
        <node role="sourceNodesQuery" type="jetbrains.mps.lang.generator.structure.SourceSubstituteMacro_SourceNodesQuery" id="797427956581992628">
          <node role="body" type="jetbrains.mps.baseLanguage.structure.StatementList" id="797427956581992629">
            <node role="statement" type="jetbrains.mps.baseLanguage.structure.ExpressionStatement" id="797427956581993149">
              <node role="expression" type="jetbrains.mps.baseLanguage.structure.DotExpression" id="797427956581993151">
                <node role="operand" type="jetbrains.mps.lang.generator.structure.TemplateFunctionParameter_sourceNode" id="797427956581993150" />
                <node role="operation" type="jetbrains.mps.lang.smodel.structure.SLinkListAccess" id="326387346505759291">
                  <link role="link" targetNodeId="1.797427956578312507" />
                </node>
              </node>
            </node>
          </node>
        </node>
      </node>
      <node role="propertyMacro$property_attribute$name" type="jetbrains.mps.lang.generator.structure.PropertyMacro" id="797427956581993156">
        <node role="propertyValueFunction" type="jetbrains.mps.lang.generator.structure.PropertyMacro_GetPropertyValue" id="797427956581993157">
          <node role="body" type="jetbrains.mps.baseLanguage.structure.StatementList" id="797427956581993158">
            <node role="statement" type="jetbrains.mps.baseLanguage.structure.ExpressionStatement" id="797427956581993159">
              <node role="expression" type="jetbrains.mps.baseLanguage.structure.DotExpression" id="797427956581993164">
                <node role="operand" type="jetbrains.mps.lang.generator.structure.TemplateFunctionParameter_sourceNode" id="797427956581993160" />
                <node role="operation" type="jetbrains.mps.lang.smodel.structure.SPropertyAccess" id="797427956581994295">
                  <link role="property" targetNodeId="2v.1169194664001" resolveInfo="name" />
                </node>
              </node>
            </node>
          </node>
        </node>
      </node>
    </node>
    <node role="visibility" type="jetbrains.mps.baseLanguage.structure.PublicVisibility" id="797427956581889118" />
    <node role="constructor" type="jetbrains.mps.baseLanguage.structure.ConstructorDeclaration" id="797427956581889119">
      <node role="returnType" type="jetbrains.mps.baseLanguage.structure.VoidType" id="797427956581889120" />
      <node role="visibility" type="jetbrains.mps.baseLanguage.structure.PublicVisibility" id="797427956581889121" />
      <node role="body" type="jetbrains.mps.baseLanguage.structure.StatementList" id="797427956581889122" />
    </node>
    <node role="rootTemplateAnnotation$attribute" type="jetbrains.mps.lang.generator.structure.RootTemplateAnnotation" id="797427956581889123">
      <link role="applicableConcept" targetNodeId="1.797427956577465754" resolveInfo="FiniteStateMachine" />
    </node>
  </node>
  <node type="jetbrains.mps.baseLanguage.structure.EnumClass" id="326387346505967490">
    <property name="name" value="Event" />
    <node role="enumConstant" type="jetbrains.mps.baseLanguage.structure.EnumConstantDeclaration" id="326387346505967497">
      <property name="name" value="name" />
      <link role="baseMethodDeclaration" targetNodeId="797427956581889119" resolveInfo="State" />
      <node role="nodeMacro$attribute" type="jetbrains.mps.lang.generator.structure.LoopMacro" id="326387346505967498">
        <node role="sourceNodesQuery" type="jetbrains.mps.lang.generator.structure.SourceSubstituteMacro_SourceNodesQuery" id="326387346505967499">
          <node role="body" type="jetbrains.mps.baseLanguage.structure.StatementList" id="326387346505967500">
            <node role="statement" type="jetbrains.mps.baseLanguage.structure.ExpressionStatement" id="326387346505967501">
              <node role="expression" type="jetbrains.mps.baseLanguage.structure.DotExpression" id="326387346505967502">
                <node role="operand" type="jetbrains.mps.lang.generator.structure.TemplateFunctionParameter_sourceNode" id="326387346505967503" />
                <node role="operation" type="jetbrains.mps.lang.smodel.structure.SLinkListAccess" id="4946881345607817422">
                  <link role="link" targetNodeId="1.797427956580782759" />
                </node>
              </node>
            </node>
          </node>
        </node>
      </node>
      <node role="propertyMacro$property_attribute$name" type="jetbrains.mps.lang.generator.structure.PropertyMacro" id="326387346505967506">
        <node role="propertyValueFunction" type="jetbrains.mps.lang.generator.structure.PropertyMacro_GetPropertyValue" id="326387346505967507">
          <node role="body" type="jetbrains.mps.baseLanguage.structure.StatementList" id="326387346505967508">
            <node role="statement" type="jetbrains.mps.baseLanguage.structure.ExpressionStatement" id="326387346505967509">
              <node role="expression" type="jetbrains.mps.baseLanguage.structure.DotExpression" id="326387346505968049">
                <node role="operand" type="jetbrains.mps.lang.generator.structure.TemplateFunctionParameter_sourceNode" id="326387346505967511" />
                <node role="operation" type="jetbrains.mps.lang.smodel.structure.SPropertyAccess" id="4946881345607823784">
                  <link role="property" targetNodeId="2v.1169194664001" resolveInfo="name" />
                </node>
              </node>
            </node>
          </node>
        </node>
      </node>
    </node>
    <node role="visibility" type="jetbrains.mps.baseLanguage.structure.PublicVisibility" id="326387346505967491" />
    <node role="constructor" type="jetbrains.mps.baseLanguage.structure.ConstructorDeclaration" id="326387346505967492">
      <node role="returnType" type="jetbrains.mps.baseLanguage.structure.VoidType" id="326387346505967493" />
      <node role="visibility" type="jetbrains.mps.baseLanguage.structure.PublicVisibility" id="326387346505967494" />
      <node role="body" type="jetbrains.mps.baseLanguage.structure.StatementList" id="326387346505967495" />
    </node>
    <node role="rootTemplateAnnotation$attribute" type="jetbrains.mps.lang.generator.structure.RootTemplateAnnotation" id="326387346505967496">
      <link role="applicableConcept" targetNodeId="1.797427956580782756" resolveInfo="EventContainer" />
    </node>
  </node>
</model>

