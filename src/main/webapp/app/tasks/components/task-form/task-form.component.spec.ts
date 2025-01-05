import {
  ComponentFixture,
  fakeAsync,
  TestBed,
  waitForAsync,
} from "@angular/core/testing";
import { of } from "rxjs";

import { TaskFormComponent } from "./task-form.component";
import { TaskService } from "app/tasks/services/task.service";
import { ElementRef } from "@angular/core";

describe("TaskFormComponent", () => {
  let component: TaskFormComponent;
  let fixture: ComponentFixture<TaskFormComponent>;
  let taskService: jasmine.SpyObj<TaskService>;

  beforeEach(waitForAsync(() => {
    taskService = jasmine.createSpyObj("taskService", ["create"]);
    TestBed.configureTestingModule({
      declarations: [TaskFormComponent],
      providers: [
        {
          provide: "TaskService",
          useValue: taskService,
        },
      ],
    })
      .overrideTemplate(TaskFormComponent, "")
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskFormComponent);
    component = fixture.componentInstance;

    const mockFileInputElement = {
      nativeElement: {
        value: "",
      },
    };

    component.fileInputElement = mockFileInputElement as ElementRef;

    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });

  it("should validate a task", () => {
    expect(component.taskForm.invalid).toBe(true);

    component.taskForm.setValue({
      name: "My task",
      date: new Date(),
      time: null,
    });

    expect(component.taskForm.invalid).toBe(false);
  });

  it("should create a task", () => {
    // given
    fakeAsync(() => {
    component.taskForm.setValue({
      name: "My task",
      date: new Date(),
      time: null,
    });

    taskService.create.and.returnValue(of({ id: "id", name: "My task" }));

    const formData = new FormData();

    const taskData = {
      name: "My task",
    };

    formData.append(
      "taskRequest",
      new Blob([JSON.stringify(taskData)], {
        type: "application/json",
      })
    );

    // when
    component.onSubmit();

    // then
    expect(taskService.create).toHaveBeenCalledWith(formData);
  });
});

  it("should emit the task after creation", () => {
    // given
    fakeAsync(() => {
    component.taskForm.setValue({
      name: "My task",
      date: new Date(),
      time: null,
    });

    taskService.create.and.returnValue(of({ id: "id", name: "My task" }));
    const createEmitter = spyOn(component.created, "emit");

    // when
    component.onSubmit();

    // then
    expect(createEmitter).toHaveBeenCalledWith({ id: "id", name: "My task" });
  });
});

  it("should reset the form after creation", () => {
    // given
    fakeAsync(() => {
      component.taskForm.setValue({
        name: "My task",
        date: new Date(),
        time: null,
      });
      taskService.create.and.returnValue(of({ id: "id", name: "My task" }));
      const formReset = spyOn(component.taskForm, "reset");

      // when
      component.onSubmit();

      // then
      expect(component.fileInputElement.nativeElement.value).toBe("");
      expect(formReset).toHaveBeenCalled();
    });
  });
});
