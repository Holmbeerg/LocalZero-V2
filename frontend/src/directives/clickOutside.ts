import type { DirectiveBinding, App } from 'vue';

declare module '@vue/runtime-core' {
  interface HTMLAttributes {
    _clickOutsideHandler?: (event: Event) => void;
  }
}

interface ClickOutsideElement extends HTMLElement {
  _clickOutsideHandler?: (event: Event) => void;
}

const clickOutside = {
  beforeMount(el: ClickOutsideElement, binding: DirectiveBinding) {
    const clickHandler = (event: Event) => {
      if (!(el === event.target || el.contains(event.target as Node))) {
        binding.value(event);
      }
    };

    el._clickOutsideHandler = clickHandler;
    document.addEventListener('click', clickHandler);
  },
  unmounted(el: ClickOutsideElement) {
    if (el._clickOutsideHandler) {
      document.removeEventListener('click', el._clickOutsideHandler);
      delete el._clickOutsideHandler;
    }
  }
};

export default {
  install: (app: App) => {
    app.directive('click-outside', clickOutside);
  }
};
