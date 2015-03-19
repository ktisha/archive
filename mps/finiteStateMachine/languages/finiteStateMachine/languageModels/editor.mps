<?xml version="1.0" encoding="UTF-8"?>
<model modelUID="r:ebdb9db2-c15f-4721-8285-9ae82882ad84(tuzova.finiteStateMachine.editor)">
  <persistence version="3" />
  <refactoringHistory />
  <language namespace="18bc6592-03a6-4e29-a83a-7ff23bde13ba(jetbrains.mps.lang.editor)" />
  <language namespace="f3061a53-9226-4cc5-a443-f952ceaf5816(jetbrains.mps.baseLanguage)" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c895902fb(jetbrains.mps.lang.smodel.constraints)" version="21" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c89590298(jetbrains.mps.lang.editor.constraints)" version="21" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c89590338(jetbrains.mps.baseLanguage.closures.structure)" version="3" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c895902db(jetbrains.mps.baseLanguage.blTypes.constraints)" version="0" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c8959028c(jetbrains.mps.lang.structure.constraints)" version="11" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c89590283(jetbrains.mps.lang.core.constraints)" version="2" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c895902ba(jetbrains.mps.lang.sharedConcepts.constraints)" version="0" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c895902c1(jetbrains.mps.baseLanguage.constraints)" version="83" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c89590292(jetbrains.mps.lang.structure.structure)" version="0" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c89590334(jetbrains.mps.baseLanguage.closures.constraints)" version="2" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c89590301(jetbrains.mps.lang.smodel.structure)" version="16" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c895902bc(jetbrains.mps.lang.sharedConcepts.structure)" version="0" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c895902ca(jetbrains.mps.baseLanguage.structure)" version="1" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c8959029e(jetbrains.mps.lang.editor.structure)" version="32" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c8959033d(jetbrains.mps.lang.annotations.structure)" version="0" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c89590288(jetbrains.mps.lang.core.structure)" version="0" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c895902ae(jetbrains.mps.lang.typesystem.constraints)" version="17" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c89590340(jetbrains.mps.lang.pattern.constraints)" version="2" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c89590345(jetbrains.mps.lang.pattern.structure)" version="0" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c89590328(jetbrains.mps.baseLanguage.collections.constraints)" version="6" />
  <languageAspect modelUID="r:309aeee7-bee8-445c-b31d-35928d1da75f(jetbrains.mps.baseLanguage.tuples.structure)" version="2" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c895902b4(jetbrains.mps.lang.typesystem.structure)" version="3" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c8959034b(jetbrains.mps.lang.quotation.structure)" version="0" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c8959032e(jetbrains.mps.baseLanguage.collections.structure)" version="7" />
  <devkit namespace="2677cb18-f558-4e33-bc38-a5139cee06dc(jetbrains.mps.devkit.language-design)" />
  <maxImportIndex value="1" />
  <import index="1" modelUID="r:de69fd3c-5806-478d-b028-6c2c64d68039(tuzova.finiteStateMachine.structure)" version="-1" />
  <visible index="2" modelUID="r:00000000-0000-4000-0000-011c89590288(jetbrains.mps.lang.core.structure)" />
  <node type="jetbrains.mps.lang.editor.structure.ConceptEditorDeclaration" id="797427956577465755">
    <link role="conceptDeclaration" targetNodeId="1.797427956577465754" />
    <node role="cellModel" type="jetbrains.mps.lang.editor.structure.CellModel_Collection" id="797427956577465764">
      <node role="childCellModel" type="jetbrains.mps.lang.editor.structure.CellModel_Constant" id="797427956577465767">
        <property name="text" value="FSMachine" />
      </node>
      <node role="childCellModel" type="jetbrains.mps.lang.editor.structure.CellModel_Property" id="797427956577465769">
        <link role="relationDeclaration" targetNodeId="2v.1169194664001" resolveInfo="name" />
        <node role="styleItem" type="jetbrains.mps.lang.editor.structure.IndentLayoutNewLineStyleClassItem" id="797427956577894084">
          <property name="flag" value="true" />
        </node>
      </node>
      <node role="childCellModel" type="jetbrains.mps.lang.editor.structure.CellModel_Constant" id="4946881345607817815">
        <property name="text" value="possible events : " />
      </node>
      <node role="childCellModel" type="jetbrains.mps.lang.editor.structure.CellModel_RefNode" id="326387346505863917">
        <link role="relationDeclaration" targetNodeId="1.326387346505863909" />
        <node role="styleItem" type="jetbrains.mps.lang.editor.structure.IndentLayoutNewLineStyleClassItem" id="326387346505863919">
          <property name="flag" value="true" />
        </node>
      </node>
      <node role="childCellModel" type="jetbrains.mps.lang.editor.structure.CellModel_RefNodeList" id="797427956581772392">
        <link role="relationDeclaration" targetNodeId="1.797427956578312507" />
        <node role="cellLayout" type="jetbrains.mps.lang.editor.structure.CellLayout_Vertical" id="797427956581772393" />
        <node role="styleItem" type="jetbrains.mps.lang.editor.structure.SelectableStyleSheetItem" id="797427956581772394">
          <property name="flag" value="false" />
        </node>
        <node role="styleItem" type="jetbrains.mps.lang.editor.structure.IndentLayoutNewLineStyleClassItem" id="4946881345607690806">
          <property name="flag" value="true" />
        </node>
      </node>
      <node role="childCellModel" type="jetbrains.mps.lang.editor.structure.CellModel_Constant" id="4946881345607955466">
        <property name="text" value="initial state : " />
      </node>
      <node role="childCellModel" type="jetbrains.mps.lang.editor.structure.CellModel_RefNode" id="4946881345607938109">
        <link role="relationDeclaration" targetNodeId="1.4946881345607938097" />
        <node role="styleItem" type="jetbrains.mps.lang.editor.structure.IndentLayoutNewLineStyleClassItem" id="4946881345607938110">
          <property name="flag" value="true" />
        </node>
      </node>
      <node role="childCellModel" type="jetbrains.mps.lang.editor.structure.CellModel_Constant" id="4946881345607955469">
        <property name="text" value="events : " />
      </node>
      <node role="childCellModel" type="jetbrains.mps.lang.editor.structure.CellModel_RefNode" id="4946881345607938112">
        <link role="relationDeclaration" targetNodeId="1.4946881345607938099" />
      </node>
      <node role="cellLayout" type="jetbrains.mps.lang.editor.structure.CellLayout_Indent" id="797427956577465766" />
    </node>
  </node>
  <node type="jetbrains.mps.lang.editor.structure.ConceptEditorDeclaration" id="797427956577578291">
    <link role="conceptDeclaration" targetNodeId="1.797427956577578290" resolveInfo="Event" />
    <node role="cellModel" type="jetbrains.mps.lang.editor.structure.CellModel_Property" id="797427956578312509">
      <link role="relationDeclaration" targetNodeId="2v.1169194664001" resolveInfo="name" />
    </node>
  </node>
  <node type="jetbrains.mps.lang.editor.structure.ConceptEditorDeclaration" id="797427956578312488">
    <link role="conceptDeclaration" targetNodeId="1.797427956578312486" resolveInfo="State" />
    <node role="cellModel" type="jetbrains.mps.lang.editor.structure.CellModel_Property" id="797427956579702748">
      <link role="relationDeclaration" targetNodeId="2v.1169194664001" resolveInfo="name" />
    </node>
  </node>
  <node type="jetbrains.mps.lang.editor.structure.ConceptEditorDeclaration" id="797427956579812125">
    <link role="conceptDeclaration" targetNodeId="1.797427956579812121" resolveInfo="StateContainer" />
    <node role="cellModel" type="jetbrains.mps.lang.editor.structure.CellModel_Collection" id="797427956579812127">
      <node role="childCellModel" type="jetbrains.mps.lang.editor.structure.CellModel_Constant" id="797427956579812130">
        <property name="text" value="state" />
      </node>
      <node role="childCellModel" type="jetbrains.mps.lang.editor.structure.CellModel_Property" id="797427956579812131">
        <link role="relationDeclaration" targetNodeId="2v.1169194664001" resolveInfo="name" />
      </node>
      <node role="childCellModel" type="jetbrains.mps.lang.editor.structure.CellModel_Constant" id="797427956579812132">
        <property name="text" value=" : " />
      </node>
      <node role="childCellModel" type="jetbrains.mps.lang.editor.structure.CellModel_RefNodeList" id="326387346505526640">
        <link role="relationDeclaration" targetNodeId="1.797427956579812124" />
        <node role="cellLayout" type="jetbrains.mps.lang.editor.structure.CellLayout_Vertical" id="326387346505526641" />
        <node role="styleItem" type="jetbrains.mps.lang.editor.structure.SelectableStyleSheetItem" id="326387346505526642">
          <property name="flag" value="false" />
        </node>
      </node>
      <node role="cellLayout" type="jetbrains.mps.lang.editor.structure.CellLayout_Indent" id="797427956579812129" />
    </node>
  </node>
  <node type="jetbrains.mps.lang.editor.structure.ConceptEditorDeclaration" id="797427956580782757">
    <link role="conceptDeclaration" targetNodeId="1.797427956580782756" resolveInfo="EventContainer" />
    <node role="cellModel" type="jetbrains.mps.lang.editor.structure.CellModel_Collection" id="797427956582845585">
      <node role="cellLayout" type="jetbrains.mps.lang.editor.structure.CellLayout_Indent" id="797427956582845586" />
      <node role="childCellModel" type="jetbrains.mps.lang.editor.structure.CellModel_RefNodeList" id="797427956582845587">
        <link role="relationDeclaration" targetNodeId="1.797427956580782759" />
        <node role="cellLayout" type="jetbrains.mps.lang.editor.structure.CellLayout_Indent" id="797427956582845588" />
      </node>
    </node>
  </node>
  <node type="jetbrains.mps.lang.editor.structure.ConceptEditorDeclaration" id="797427956581879653">
    <link role="conceptDeclaration" targetNodeId="1.797427956581879651" resolveInfo="StateContainer" />
    <node role="cellModel" type="jetbrains.mps.lang.editor.structure.CellModel_Collection" id="797427956581879657">
      <node role="childCellModel" type="jetbrains.mps.lang.editor.structure.CellModel_RefNodeList" id="797427956581879662">
        <link role="relationDeclaration" targetNodeId="1.797427956581879652" />
        <node role="cellLayout" type="jetbrains.mps.lang.editor.structure.CellLayout_Horizontal" id="797427956581879663" />
        <node role="styleItem" type="jetbrains.mps.lang.editor.structure.SelectableStyleSheetItem" id="797427956581879664">
          <property name="flag" value="false" />
        </node>
      </node>
      <node role="cellLayout" type="jetbrains.mps.lang.editor.structure.CellLayout_Indent" id="797427956581879659" />
    </node>
  </node>
  <node type="jetbrains.mps.lang.editor.structure.ConceptEditorDeclaration" id="326387346505526626">
    <link role="conceptDeclaration" targetNodeId="1.326387346505526623" resolveInfo="Pair" />
    <node role="cellModel" type="jetbrains.mps.lang.editor.structure.CellModel_Collection" id="326387346505526628">
      <node role="childCellModel" type="jetbrains.mps.lang.editor.structure.CellModel_RefNode" id="4946881345607470910">
        <link role="relationDeclaration" targetNodeId="1.326387346505526624" />
      </node>
      <node role="childCellModel" type="jetbrains.mps.lang.editor.structure.CellModel_Constant" id="326387346505526635">
        <property name="text" value=" : " />
      </node>
      <node role="childCellModel" type="jetbrains.mps.lang.editor.structure.CellModel_RefNode" id="4946881345607470912">
        <link role="relationDeclaration" targetNodeId="1.326387346505526625" />
      </node>
      <node role="cellLayout" type="jetbrains.mps.lang.editor.structure.CellLayout_Horizontal" id="326387346505526630" />
    </node>
  </node>
  <node type="jetbrains.mps.lang.editor.structure.ConceptEditorDeclaration" id="4946881345607804506">
    <link role="conceptDeclaration" targetNodeId="1.4946881345607804505" resolveInfo="Event" />
    <node role="cellModel" type="jetbrains.mps.lang.editor.structure.CellModel_Property" id="4946881345607804509">
      <link role="relationDeclaration" targetNodeId="2v.1169194664001" resolveInfo="name" />
    </node>
  </node>
</model>

