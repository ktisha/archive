<?xml version="1.0" encoding="UTF-8"?>
<model modelUID="r:ca0a075a-2e85-472c-ab4f-5a37c44f33e7(tuzova.finiteStateMachine.sandbox.sandbox)">
  <persistence version="3" />
  <refactoringHistory />
  <language namespace="00d66c09-c182-4747-84e3-58794ed31fa9(tuzova.finiteStateMachine)" />
  <language namespace="f3061a53-9226-4cc5-a443-f952ceaf5816(jetbrains.mps.baseLanguage)" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c89590288(jetbrains.mps.lang.core.structure)" version="0" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c89590283(jetbrains.mps.lang.core.constraints)" version="2" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c895902c1(jetbrains.mps.baseLanguage.constraints)" version="83" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c895902db(jetbrains.mps.baseLanguage.blTypes.constraints)" version="0" />
  <languageAspect modelUID="r:309aeee7-bee8-445c-b31d-35928d1da75f(jetbrains.mps.baseLanguage.tuples.structure)" version="2" />
  <languageAspect modelUID="r:00000000-0000-4000-0000-011c895902ca(jetbrains.mps.baseLanguage.structure)" version="1" />
  <maxImportIndex value="3" />
  <import index="1" modelUID="f:java_stub#java.lang(java.lang@java_stub)" version="-1" />
  <import index="2" modelUID="f:java_stub#org.apache.commons.lang.enum(org.apache.commons.lang.enum@java_stub)" version="-1" />
  <import index="3" modelUID="f:java_stub#org.apache.commons.lang.enums(org.apache.commons.lang.enums@java_stub)" version="-1" />
  <node type="tuzova.finiteStateMachine.structure.FiniteStateMachine" id="797427956577578289">
    <property name="name" value="myFSMachine" />
    <node role="stateRule" type="tuzova.finiteStateMachine.structure.StateRule" id="326387346504883915">
      <property name="name" value="state" />
      <node role="pair" type="tuzova.finiteStateMachine.structure.Pair" id="326387346505642890">
        <node role="event" type="tuzova.finiteStateMachine.structure.Event" id="326387346505642891">
          <property name="name" value="event" />
        </node>
        <node role="state" type="tuzova.finiteStateMachine.structure.State" id="326387346505642892">
          <property name="name" value="state" />
          <node role="componentType" type="jetbrains.mps.baseLanguage.structure.Type" id="326387346505642893" />
        </node>
      </node>
      <node role="pair" type="tuzova.finiteStateMachine.structure.Pair" id="326387346505649597">
        <node role="event" type="tuzova.finiteStateMachine.structure.Event" id="326387346505649598">
          <property name="name" value="event1" />
        </node>
        <node role="state" type="tuzova.finiteStateMachine.structure.State" id="326387346505649599">
          <property name="name" value="state1" />
          <node role="componentType" type="jetbrains.mps.baseLanguage.structure.Type" id="326387346505649600" />
        </node>
      </node>
      <node role="pair" type="tuzova.finiteStateMachine.structure.Pair" id="326387346505754540">
        <node role="event" type="tuzova.finiteStateMachine.structure.Event" id="326387346505754541">
          <property name="name" value="event2" />
        </node>
        <node role="state" type="tuzova.finiteStateMachine.structure.State" id="326387346505754542">
          <property name="name" value="state" />
          <node role="componentType" type="jetbrains.mps.baseLanguage.structure.Type" id="326387346505754543" />
        </node>
      </node>
      <node role="state" type="tuzova.finiteStateMachine.structure.State" id="326387346504883916">
        <property name="name" value="state1" />
        <node role="componentType" type="jetbrains.mps.baseLanguage.structure.Type" id="326387346504883917" />
      </node>
      <node role="state" type="tuzova.finiteStateMachine.structure.State" id="326387346505314736">
        <property name="name" value="state2" />
        <node role="componentType" type="jetbrains.mps.baseLanguage.structure.Type" id="326387346505314737" />
      </node>
      <node role="event" type="tuzova.finiteStateMachine.structure.Event" id="326387346504883918">
        <property name="name" value="event1" />
      </node>
      <node role="event" type="tuzova.finiteStateMachine.structure.Event" id="326387346505314735">
        <property name="name" value="event2" />
      </node>
    </node>
    <node role="stateRule" type="tuzova.finiteStateMachine.structure.StateRule" id="326387346505207051">
      <property name="name" value="state1" />
      <node role="pair" type="tuzova.finiteStateMachine.structure.Pair" id="326387346505649610">
        <node role="event" type="tuzova.finiteStateMachine.structure.Event" id="326387346505649611">
          <property name="name" value="event" />
        </node>
        <node role="state" type="tuzova.finiteStateMachine.structure.State" id="326387346505649612">
          <property name="name" value="state1" />
          <node role="componentType" type="jetbrains.mps.baseLanguage.structure.Type" id="326387346505649613" />
        </node>
      </node>
      <node role="pair" type="tuzova.finiteStateMachine.structure.Pair" id="326387346505649614">
        <node role="event" type="tuzova.finiteStateMachine.structure.Event" id="326387346505649615">
          <property name="name" value="event1" />
        </node>
        <node role="state" type="tuzova.finiteStateMachine.structure.State" id="326387346505649616">
          <property name="name" value="state" />
          <node role="componentType" type="jetbrains.mps.baseLanguage.structure.Type" id="326387346505649617" />
        </node>
      </node>
      <node role="pair" type="tuzova.finiteStateMachine.structure.Pair" id="4946881345607467577">
        <node role="event" type="tuzova.finiteStateMachine.structure.Event" id="4946881345607467578">
          <property name="name" value="event2" />
        </node>
        <node role="state" type="tuzova.finiteStateMachine.structure.State" id="4946881345607467579">
          <property name="name" value="state" />
          <node role="componentType" type="jetbrains.mps.baseLanguage.structure.Type" id="4946881345607467580" />
        </node>
      </node>
      <node role="state" type="tuzova.finiteStateMachine.structure.State" id="326387346505314739">
        <property name="name" value="state1" />
        <node role="componentType" type="jetbrains.mps.baseLanguage.structure.Type" id="326387346505314740" />
      </node>
      <node role="state" type="tuzova.finiteStateMachine.structure.State" id="326387346505314741">
        <property name="name" value="state2" />
        <node role="componentType" type="jetbrains.mps.baseLanguage.structure.Type" id="326387346505314742" />
      </node>
      <node role="event" type="tuzova.finiteStateMachine.structure.Event" id="326387346505207054">
        <property name="name" value="event1" />
      </node>
      <node role="event" type="tuzova.finiteStateMachine.structure.Event" id="326387346505314738">
        <property name="name" value="event2" />
      </node>
    </node>
    <node role="state" type="tuzova.finiteStateMachine.structure.State" id="797427956579812111">
      <property name="name" value="state1" />
      <node role="componentType" type="jetbrains.mps.baseLanguage.structure.Type" id="797427956579812112" />
    </node>
    <node role="event" type="tuzova.finiteStateMachine.structure.Event" id="797427956577997421">
      <property name="name" value="first" />
    </node>
    <node role="event" type="tuzova.finiteStateMachine.structure.Event" id="797427956578422808">
      <property name="name" value="second" />
    </node>
    <node role="eventContainer" type="tuzova.finiteStateMachine.structure.EventContainer" id="797427956580886416">
      <node role="event" type="tuzova.finiteStateMachine.structure.Event" id="326387346506184983">
        <property name="name" value="event" />
      </node>
      <node role="event" type="tuzova.finiteStateMachine.structure.Event" id="326387346506184984">
        <property name="name" value="event1" />
      </node>
      <node role="event" type="tuzova.finiteStateMachine.structure.Event" id="326387346506184985">
        <property name="name" value="event2" />
      </node>
    </node>
    <node role="stateContainer" type="tuzova.finiteStateMachine.structure.StateContainer" id="797427956582635040">
      <node role="state" type="tuzova.finiteStateMachine.structure.State" id="797427956582635041">
        <property name="name" value="st" />
        <node role="componentType" type="jetbrains.mps.baseLanguage.structure.Type" id="797427956582635042" />
      </node>
      <node role="state" type="tuzova.finiteStateMachine.structure.State" id="797427956582635043">
        <property name="name" value="st1" />
        <node role="componentType" type="jetbrains.mps.baseLanguage.structure.Type" id="797427956582635044" />
      </node>
    </node>
    <node role="initState" type="tuzova.finiteStateMachine.structure.State" id="4946881345607955461">
      <property name="name" value="state" />
    </node>
    <node role="events" type="tuzova.finiteStateMachine.structure.EventContainer" id="4946881345607955462">
      <node role="event" type="tuzova.finiteStateMachine.structure.Event" id="4946881345607955463">
        <property name="name" value="event1" />
      </node>
      <node role="event" type="tuzova.finiteStateMachine.structure.Event" id="4946881345607955464">
        <property name="name" value="event1" />
      </node>
    </node>
  </node>
</model>

