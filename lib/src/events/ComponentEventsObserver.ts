import * as _ from 'lodash';
import { EventSubscription } from '../interfaces/EventSubscription';
import {
  ComponentDidAppearEvent,
  ComponentDidDisappearEvent,
  NavigationButtonPressedEvent,
  SearchBarUpdatedEvent,
  SearchBarCancelPressedEvent,
  ComponentEvent,
  PreviewCompletedEvent,
  ModalDismissedEvent
} from '../interfaces/ComponentEvents';
import { EventsRegistry } from './EventsRegistry';

type ReactComponentWithIndexing = React.Component<any> & Record<string, any>;

export class ComponentEventsObserver {
  private listeners: Record<string, Record<string, ReactComponentWithIndexing>> = {};
  private alreadyRegistered = false;

  constructor() {
    this.notifyComponentDidAppear = this.notifyComponentDidAppear.bind(this);
    this.notifyComponentDidDisappear = this.notifyComponentDidDisappear.bind(this);
    this.notifyNavigationButtonPressed = this.notifyNavigationButtonPressed.bind(this);
    this.notifyModalDismissed = this.notifyModalDismissed.bind(this);
    this.notifySearchBarUpdated = this.notifySearchBarUpdated.bind(this);
    this.notifySearchBarCancelPressed = this.notifySearchBarCancelPressed.bind(this);
    this.notifyPreviewCompleted = this.notifyPreviewCompleted.bind(this);
  }

  public registerOnceForAllComponentEvents(eventsRegistry: EventsRegistry) {
    if (this.alreadyRegistered) { return; }
    this.alreadyRegistered = true;
    eventsRegistry.registerComponentDidAppearListener(this.notifyComponentDidAppear);
    eventsRegistry.registerComponentDidDisappearListener(this.notifyComponentDidDisappear);
    eventsRegistry.registerNavigationButtonPressedListener(this.notifyNavigationButtonPressed);
    eventsRegistry.registerModalDismissedListener(this.notifyModalDismissed);
    eventsRegistry.registerSearchBarUpdatedListener(this.notifySearchBarUpdated);
    eventsRegistry.registerSearchBarCancelPressedListener(this.notifySearchBarCancelPressed);
    eventsRegistry.registerPreviewCompletedListener(this.notifyPreviewCompleted);
  }

  public bindComponent(component: React.Component<any>, componentId?: string): EventSubscription {
    const computedComponentId = componentId || component.props.componentId;

    if (!_.isString(computedComponentId)) {
      throw new Error(`bindComponent expects a component with a componentId in props or a componentId as the second argument`);
    }
    if (_.isNil(this.listeners[computedComponentId])) {
      this.listeners[computedComponentId] = {};
    }
    const key = _.uniqueId();
    this.listeners[computedComponentId][key] = component;

    return { remove: () => _.unset(this.listeners[computedComponentId], key) };
  }

  public unmounted(componentId: string) {
    _.unset(this.listeners, componentId);
  }

  notifyComponentDidAppear(event: ComponentDidAppearEvent) {
    this.triggerOnAllListenersByComponentId(event, 'componentDidAppear');
  }

  notifyComponentDidDisappear(event: ComponentDidDisappearEvent) {
    this.triggerOnAllListenersByComponentId(event, 'componentDidDisappear');
  }

  notifyNavigationButtonPressed(event: NavigationButtonPressedEvent) {
    this.triggerOnAllListenersByComponentId(event, 'navigationButtonPressed');
  }

  notifyModalDismissed(event: ModalDismissedEvent) {
    this.triggerOnAllListenersByComponentId(event, 'modalDismissed');
  }

  notifySearchBarUpdated(event: SearchBarUpdatedEvent) {
    this.triggerOnAllListenersByComponentId(event, 'searchBarUpdated');
  }

  notifySearchBarCancelPressed(event: SearchBarCancelPressedEvent) {
    this.triggerOnAllListenersByComponentId(event, 'searchBarCancelPressed');
  }

  notifyPreviewCompleted(event: PreviewCompletedEvent) {
    this.triggerOnAllListenersByComponentId(event, 'previewCompleted');
  }

  private triggerOnAllListenersByComponentId(event: ComponentEvent, method: string) {
    _.forEach(this.listeners[event.componentId], (component) => {
      if (component[method]) {
        component[method](event);
      }
    });
  }
}
