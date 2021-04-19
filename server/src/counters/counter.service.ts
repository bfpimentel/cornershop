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
    const counterToBeDeleted = counters[index];
    delete counters[index];
    console.log(`SYNC: Deleted counter ${counterToBeDeleted}`);
  });

  Array.from(sync.counters).forEach((newCounter) => {
    const index = counters.findIndex((counter) => counter.id == newCounter.id);

    if (index == -1) {
      counters.push(newCounter);
      console.log(`SYNC: Added new counter ${newCounter}`);
    } else {
      counters[index] = newCounter;
      console.log(`SYNC: Updated counter ${newCounter}`);
    }
  });

  return getAll();
};
