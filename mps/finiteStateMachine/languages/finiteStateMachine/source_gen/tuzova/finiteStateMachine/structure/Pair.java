package tuzova.finiteStateMachine.structure;

/*Generated by MPS */

import jetbrains.mps.lang.core.structure.BaseConcept;
import jetbrains.mps.smodel.SNode;
import jetbrains.mps.smodel.SModel;
import jetbrains.mps.smodel.SModelUtil_new;
import jetbrains.mps.project.GlobalScope;

public class Pair extends BaseConcept {
  public static final String concept = "tuzova.finiteStateMachine.structure.Pair";
  public static final String EVENT = "event";
  public static final String STATE = "state";

  public Pair(SNode node) {
    super(node);
  }

  public Event getEvent() {
    return (Event)this.getChild(Event.class, Pair.EVENT);
  }

  public void setEvent(Event node) {
    super.setChild(Pair.EVENT, node);
  }

  public State getState() {
    return (State)this.getChild(State.class, Pair.STATE);
  }

  public void setState(State node) {
    super.setChild(Pair.STATE, node);
  }


  public static Pair newInstance(SModel sm, boolean init) {
    return (Pair)SModelUtil_new.instantiateConceptDeclaration("tuzova.finiteStateMachine.structure.Pair", sm, GlobalScope.getInstance(), init).getAdapter();
  }

  public static Pair newInstance(SModel sm) {
    return Pair.newInstance(sm, false);
  }

}
