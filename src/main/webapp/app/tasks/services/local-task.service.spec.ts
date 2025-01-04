import { TestBed } from "@angular/core/testing";
import { LocalTaskService } from "./local-task.service";
import { TaskRequest } from "../models/task-request.model";
import { Task } from "../models/task.model";

describe("LocalTaskService", () => {
  let service: LocalTaskService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LocalTaskService],
    });
    service = TestBed.inject(LocalTaskService);

    // Mock localStorage
    spyOn(localStorage, "getItem").and.callFake((key: string) => mockStorage[key] || null);
    spyOn(localStorage, "setItem").and.callFake((key: string, value: string) => {
      mockStorage[key] = value;
    });
  });

  let mockStorage: Record<string, string> = {};

  beforeEach(() => {
    mockStorage = {};
  });

  describe("getAll", () => {
    it("should return all tasks from localStorage", (done) => {
      const tasks: Task[] = [
        { id: "1", name: "Task 1", dueDate: "2025-01-05" },
        { id: "2", name: "Task 2", dueDate: "2025-01-06" },
      ];
      mockStorage[LocalTaskService.STORAGE_KEY] = JSON.stringify(tasks);

      service.getAll().subscribe((result) => {
        expect(result).toEqual(tasks);
        done();
      });
    });

    it("should return an empty array if no tasks are in localStorage", (done) => {
      mockStorage[LocalTaskService.STORAGE_KEY] = null;

      service.getAll().subscribe((result) => {
        expect(result).toEqual([]);
        done();
      });
    });
  });

  describe("create", () => {
    it("should create a new task and save it to localStorage", (done) => {
      const taskRequest: TaskRequest = { name: "New Task", dueDate: "2025-01-07" };
      const formData = new FormData();
      formData.append("taskRequest", new Blob([JSON.stringify(taskRequest)], { type: "application/json" }));

      spyOn<any>(service, "readTasks").and.returnValue([]);
      spyOn<any>(service, "writeTasks").and.callFake((tasks: Task[]) => {
        mockStorage[LocalTaskService.STORAGE_KEY] = JSON.stringify(tasks);
      });

      service.create(formData).subscribe((task: Task) => {
        expect(task.name).toEqual("New Task");
        expect(task.dueDate).toEqual("2025-01-07");

        const savedTasks = JSON.parse(mockStorage[LocalTaskService.STORAGE_KEY]);
        expect(savedTasks.length).toBe(1);
        expect(savedTasks[0].name).toBe("New Task");
        done();
      });
    });

    it("should throw an error if taskRequest data is missing", async () => {
      const formData = new FormData(); // No taskRequest appended
    
      try {
        await service.create(formData);
        fail('Expected an error to be thrown'); 
      } catch (error) {
        expect(error.message).toBe('No taskRequest data found in FormData');
      }
    });
  });

  describe("delete", () => {
    it("should delete a task by id", (done) => {
      const tasks: Task[] = [
        { id: "1", name: "Task 1", dueDate: "2025-01-05" },
        { id: "2", name: "Task 2", dueDate: "2025-01-06" },
      ];
      mockStorage[LocalTaskService.STORAGE_KEY] = JSON.stringify(tasks);

      service.delete("1").subscribe(() => {
        const updatedTasks = JSON.parse(mockStorage[LocalTaskService.STORAGE_KEY]);
        expect(updatedTasks.length).toBe(1);
        expect(updatedTasks[0].id).toBe("2");
        done();
      });
    });

    it("should do nothing if the task id does not exist", (done) => {
      const tasks: Task[] = [
        { id: "1", name: "Task 1", dueDate: "2025-01-05" },
      ];
      mockStorage[LocalTaskService.STORAGE_KEY] = JSON.stringify(tasks);

      service.delete("999").subscribe(() => {
        const updatedTasks = JSON.parse(mockStorage[LocalTaskService.STORAGE_KEY]);
        expect(updatedTasks).toEqual(tasks);
        done();
      });
    });
  });

  describe("readTasks", () => {
    it("should read and parse tasks from localStorage", () => {
      const tasks: Task[] = [{ id: "1", name: "Task 1", dueDate: "2025-01-05" }];
      mockStorage[LocalTaskService.STORAGE_KEY] = JSON.stringify(tasks);

      const result = service["readTasks"]();
      expect(result).toEqual(tasks);
    });

    it("should return an empty array if localStorage is empty", () => {
      mockStorage[LocalTaskService.STORAGE_KEY] = null;

      const result = service["readTasks"]();
      expect(result).toEqual([]);
    });
  });

  describe("writeTasks", () => {
    it("should write tasks to localStorage", () => {
      const tasks: Task[] = [{ id: "1", name: "Task 1", dueDate: "2025-01-05" }];

      service["writeTasks"](tasks);

      const savedTasks = JSON.parse(mockStorage[LocalTaskService.STORAGE_KEY]);
      expect(savedTasks).toEqual(tasks);
    });
  });
});