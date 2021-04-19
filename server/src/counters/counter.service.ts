import { Counter, Sync } from "./counter.interface";

let counters: Counter[] = [];

export const getAll = async (): Promise<Counter[]> => counters;

export const create = async (title: string): Promise<Counter> => {
  const newItemId = (+new Date() + ~~(Math.random() * 999999)).toString(36);

  const newItem: Counter = {
    id: newItemId,
    title: title,
    count: 0,
  };

  counters.push(newItem);

  return newItem;
};

export const sync = async (sync: Sync): Promise<Counter[]> => {
  Array.from(sync.deletedCounterIds).forEach((id) => {
    const index = counters.findIndex((counter) => counter.id == id);
    counters.splice(index, 1);
  });

  Array.from(sync.counters).forEach((counter) => {
    const index = counters.findIndex((counter) => counter.id == counter.id);

    if (index == -1) counters.push(counter);
    else counters[index] = counter;
  });

  return getAll();
};
