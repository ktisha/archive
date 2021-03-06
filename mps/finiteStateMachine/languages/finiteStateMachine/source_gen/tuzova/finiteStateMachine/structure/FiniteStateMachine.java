package tuzova.finiteStateMachine.structure;

/*Generated by MPS */

import jetbrains.mps.lang.core.structure.BaseConcept;
import jetbrains.mps.lang.core.structure.INamedConcept;
import jetbrains.mps.smodel.SNode;
import java.util.Iterator;
import java.util.List;
import jetbrains.mps.smodel.SModel;
import jetbrains.mps.smodel.SModelUtil_new;
import jetbrains.mps.project.GlobalScope;

public class FiniteStateMachine extends BaseConcept implements INamedConcept {
  public static final String concept = "tuzova.finiteStateMachine.structure.FiniteStateMachine";
  public static final String NAME = "name";
  public static final String SHORT_DESCRIPTION = "shortDescription";
  public static final String ALIAS = "alias";
  public static final String VIRTUAL_PACKAGE = "virtualPackage";
  public static final String EVENT_CONTAINER = "eventContainer";
  public static final String INIT_STATE = "initState";
  public static final String EVENTS = "events";
  public static final String STATE_RULE = "stateRule";

  public FiniteStateMachine(SNode node) {
    super(node);
  }

  public String getName() {
    return this.getProperty(FiniteStateMachine.NAME);
  }

  public void setName(String value) {
    this.setProperty(FiniteStateMachine.NAME, value);
  }

  public String getShortDescription() {
    return this.getProperty(FiniteStateMachine.SHORT_DESCRIPTION);
  }

  public void setShortDescription(String value) {
    this.setProperty(FiniteStateMachine.SHORT_DESCRIPTION, value);
  }

  public String getAlias() {
    return this.getProperty(FiniteStateMachine.ALIAS);
  }

  public void setAlias(String value) {
    this.setProperty(FiniteStateMachine.ALIAS, value);
  }

  public String getVirtualPackage() {
    return this.getProperty(FiniteStateMachine.VIRTUAL_PACKAGE);
  }

  public void setVirtualPackage(String value) {
    this.setProperty(FiniteStateMachine.VIRTUAL_PACKAGE, value);
  }

  public EventContainer getEventContainer() {
    return (EventContainer)this.getChild(EventContainer.class, FiniteStateMachine.EVENT_CONTAINER);
  }

  public void setEventContainer(EventContainer node) {
    super.setChild(FiniteStateMachine.EVENT_CONTAINER, node);
  }

  public State getInitState() {
    return (State)this.getChild(State.class, FiniteStateMachine.INIT_STATE);
  }

  public void setInitState(State node) {
    super.setChild(FiniteStateMachine.INIT_STATE, node);
  }

  public EventContainer getEvents() {
    return (EventContainer)this.getChild(EventContainer.class, FiniteStateMachine.EVENTS);
  }

  public void setEvents(EventContainer node) {
    super.setChild(FiniteStateMachine.EVENTS, node);
  }

  public int getStateRulesCount() {
    return this.getChildCount(FiniteStateMachine.STATE_RULE);
  }

  public Iterator<StateRule> stateRules() {
    return this.children(StateRule.class, FiniteStateMachine.STATE_RULE);
  }

  public List<StateRule> getStateRules() {
    return this.getChildren(StateRule.class, FiniteStateMachine.STATE_RULE);
  }

  public void addStateRule(StateRule node) {
    this.addChild(FiniteStateMachine.STATE_RULE, node);
  }

  public void insertStateRule(StateRule prev, StateRule node) {
    this.insertChild(prev, FiniteStateMachine.STATE_RULE, node);
  }


  public static FiniteStateMachine newInstance(SModel sm, boolean init) {
    return (FiniteStateMachine)SModelUtil_new.instantiateConceptDeclaration("tuzova.finiteStateMachine.structure.FiniteStateMachine", sm, GlobalScope.getInstance(), init).getAdapter();
  }

  public static FiniteStateMachine newInstance(SModel sm) {
    return FiniteStateMachine.newInstance(sm, false);
  }

}
